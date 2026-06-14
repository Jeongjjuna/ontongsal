<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import type {
  CreateChildEventRequest,
  CreateEventWithItemsRequest,
  CreateParentEventRequest,
  CreatedEventId,
  EventMasterRowResponse,
  EventMasterTreeResponse,
  UpdateChildEventRequest,
  UpdateParentEventRequest,
} from '~/api/eventAdmin'
import type { EventResponse } from '~/api/event'
import type { CompletionCondition, EventType, RewardType } from '~/api/types'

definePageMeta({ layout: 'admin' })

const { $api } = useNuxtApp()
const ADMIN_EVENTS_PATH = '/api/admin/events'

// ---------------------------------------------------------------------------
// 데이터 로딩: 관리 화면은 raw 트리, 상태 배지는 structured 응답에서 가져온다.
// ---------------------------------------------------------------------------

const {
  data: trees,
  pending: pendingTrees,
  error: errorTrees,
  refresh: refreshTrees,
} = await useAsyncData<EventMasterTreeResponse[]>(
    'admin-events-raw',
    () => $api<EventMasterTreeResponse[]>(`${ADMIN_EVENTS_PATH}/raw`),
    { default: () => [] as EventMasterTreeResponse[] },
)

const {
  data: structured,
  refresh: refreshStructured,
} = await useAsyncData<EventResponse[]>(
    'admin-events-structured',
    () => $api<EventResponse[]>(ADMIN_EVENTS_PATH),
    { default: () => [] as EventResponse[] },
)

const statusById = computed(() => {
  const map = new Map<number, EventResponse['status']>()
  for (const e of structured.value ?? []) map.set(e.id, e.status)
  return map
})

// 원본 테이블 뷰용: 트리를 부모→자식 순으로 펼친 행 목록.
type FlatRow = EventMasterRowResponse & { isParentRow: boolean }
const flatRows = computed<FlatRow[]>(() => {
  const rows: FlatRow[] = []
  for (const tree of trees.value ?? []) {
    rows.push({ ...tree.parent, isParentRow: true })
    for (const child of tree.children) {
      rows.push({ ...child, isParentRow: false })
    }
  }
  return rows
})

const rawColumns = [
  'eventId',
  'parentEventId',
  'type',
  'name',
  'isActive',
  'startedAt',
  'endedAt',
  'completionCondition',
  'reward',
  'day',
] as const

async function refreshAll(): Promise<void> {
  await Promise.all([refreshTrees(), refreshStructured()])
}

// ---------------------------------------------------------------------------
// 보기 모드 토글: 관리 카드 ↔ 원본 JSON
// ---------------------------------------------------------------------------

type ViewMode = 'manage' | 'raw'
const viewMode = ref<ViewMode>('manage')

// ---------------------------------------------------------------------------
// 모달 상태
// ---------------------------------------------------------------------------

type ModalState =
    | { kind: 'none' }
    | { kind: 'createParent' }
    | { kind: 'updateParent'; eventId: number }
    | { kind: 'createChild'; parentId: number; parentType: EventType }
    | { kind: 'updateChild'; childEventId: number; parentType: EventType }

const modal = ref<ModalState>({ kind: 'none' })
const submitting = ref(false)
const modalError = ref<string | null>(null)

// ---------------------------------------------------------------------------
// 폼 상태
// ---------------------------------------------------------------------------

type ItemDraft = {
  name: string
  reward: RewardType
  completionCondition: CompletionCondition
  day: number
}

const parentForm = reactive({
  type: 'MISSION' as EventType,
  name: '',
  isActive: true,
  startedAt: '',
  endedAt: '',
  items: [] as ItemDraft[],
})

const childForm = reactive({
  name: '',
  reward: 'POINT' as RewardType,
  completionCondition: 'CLICK' as CompletionCondition,
  day: 1,
})

function resetParentForm(): void {
  parentForm.type = 'MISSION'
  parentForm.name = ''
  parentForm.isActive = true
  parentForm.startedAt = ''
  parentForm.endedAt = ''
  parentForm.items = []
}

function resetChildForm(): void {
  childForm.name = ''
  childForm.reward = 'POINT'
  childForm.completionCondition = 'CLICK'
  childForm.day = 1
}

function addDraftItem(): void {
  parentForm.items.push({
    name: '',
    reward: 'POINT',
    completionCondition: 'CLICK',
    day: parentForm.items.length + 1,
  })
}

function removeDraftItem(idx: number): void {
  parentForm.items.splice(idx, 1)
}

// ---------------------------------------------------------------------------
// 모달 오픈
// ---------------------------------------------------------------------------

function openCreateParent(): void {
  resetParentForm()
  modalError.value = null
  modal.value = { kind: 'createParent' }
}

function openUpdateParent(row: EventMasterRowResponse): void {
  resetParentForm()
  parentForm.type = row.type
  parentForm.name = row.name
  parentForm.isActive = row.isActive
  parentForm.startedAt = toLocalInput(row.startedAt)
  parentForm.endedAt = toLocalInput(row.endedAt)
  modalError.value = null
  modal.value = { kind: 'updateParent', eventId: row.eventId }
}

function openCreateChild(parent: EventMasterRowResponse): void {
  resetChildForm()
  childForm.day = (parent.type === 'ATTENDANCE') ? nextDay(parent.eventId) : 1
  modalError.value = null
  modal.value = { kind: 'createChild', parentId: parent.eventId, parentType: parent.type }
}

function openUpdateChild(parent: EventMasterRowResponse, child: EventMasterRowResponse): void {
  childForm.name = child.name
  childForm.reward = child.reward ?? 'POINT'
  childForm.completionCondition = child.completionCondition ?? 'CLICK'
  childForm.day = child.day ?? 1
  modalError.value = null
  modal.value = { kind: 'updateChild', childEventId: child.eventId, parentType: parent.type }
}

function closeModal(): void {
  if (submitting.value) return
  modal.value = { kind: 'none' }
  modalError.value = null
}

function nextDay(parentId: number): number {
  const tree = (trees.value ?? []).find(t => t.parent.eventId === parentId)
  const max = tree?.children.reduce((acc, c) => Math.max(acc, c.day ?? 0), 0) ?? 0
  return max + 1
}

// ---------------------------------------------------------------------------
// 제출 핸들러
// ---------------------------------------------------------------------------

async function withSubmit(fn: () => Promise<unknown>): Promise<void> {
  if (submitting.value) return
  submitting.value = true
  modalError.value = null
  try {
    await fn()
    modal.value = { kind: 'none' }
    await refreshAll()
  } catch (e) {
    modalError.value = e instanceof Error ? e.message : String(e)
  } finally {
    submitting.value = false
  }
}

function submitCreateParent(): Promise<void> {
  const base: CreateParentEventRequest = {
    type: parentForm.type,
    name: parentForm.name,
    isActive: parentForm.isActive,
    startedAt: toServerIso(parentForm.startedAt),
    endedAt: toServerIso(parentForm.endedAt),
  }

  // 자식 항목이 없으면 단일 생성 API, 있으면 일괄 생성 API.
  if (parentForm.items.length === 0) {
    return withSubmit(() => $api<CreatedEventId>(ADMIN_EVENTS_PATH, {
      method: 'POST',
      body: base,
    }))
  }

  const items: CreateChildEventRequest[] = parentForm.items.map(it => buildItemPayload(parentForm.type, it))
  const body: CreateEventWithItemsRequest = { ...base, items }
  return withSubmit(() => $api<CreatedEventId>(`${ADMIN_EVENTS_PATH}/with-items`, {
    method: 'POST',
    body,
  }))
}

function submitUpdateParent(): Promise<void> {
  if (modal.value.kind !== 'updateParent') return Promise.resolve()
  const eventId = modal.value.eventId
  const body: UpdateParentEventRequest = {
    name: parentForm.name,
    isActive: parentForm.isActive,
    startedAt: toServerIso(parentForm.startedAt),
    endedAt: toServerIso(parentForm.endedAt),
  }
  return withSubmit(() => $api(`${ADMIN_EVENTS_PATH}/${eventId}`, {
    method: 'PUT',
    body,
  }))
}

function submitCreateChild(): Promise<void> {
  if (modal.value.kind !== 'createChild') return Promise.resolve()
  const { parentId, parentType } = modal.value
  const body = buildChildPayload(parentType)
  return withSubmit(() => $api<CreatedEventId>(`${ADMIN_EVENTS_PATH}/${parentId}/items`, {
    method: 'POST',
    body,
  }))
}

function submitUpdateChild(): Promise<void> {
  if (modal.value.kind !== 'updateChild') return Promise.resolve()
  const { childEventId, parentType } = modal.value
  const body: UpdateChildEventRequest = buildChildPayload(parentType)
  return withSubmit(() => $api(`${ADMIN_EVENTS_PATH}/items/${childEventId}`, {
    method: 'PUT',
    body,
  }))
}

function buildChildPayload(parentType: EventType): CreateChildEventRequest {
  return {
    name: childForm.name,
    reward: childForm.reward,
    completionCondition: parentType === 'MISSION' ? childForm.completionCondition : null,
    day: parentType === 'ATTENDANCE' ? childForm.day : null,
  }
}

function buildItemPayload(parentType: EventType, it: ItemDraft): CreateChildEventRequest {
  return {
    name: it.name,
    reward: it.reward,
    completionCondition: parentType === 'MISSION' ? it.completionCondition : null,
    day: parentType === 'ATTENDANCE' ? it.day : null,
  }
}

// ---------------------------------------------------------------------------
// 삭제 핸들러
// ---------------------------------------------------------------------------

async function onDeleteParent(row: EventMasterRowResponse): Promise<void> {
  if (!confirm(`'${row.name}' 이벤트와 모든 하위 항목을 삭제합니다. 계속할까요?`)) return
  try {
    await $api(`${ADMIN_EVENTS_PATH}/${row.eventId}`, { method: 'DELETE' })
    await refreshAll()
  } catch (e) {
    alert(e instanceof Error ? e.message : String(e))
  }
}

async function onDeleteChild(child: EventMasterRowResponse): Promise<void> {
  if (!confirm(`'${child.name}' 하위 항목을 삭제합니다.`)) return
  try {
    await $api(`${ADMIN_EVENTS_PATH}/items/${child.eventId}`, { method: 'DELETE' })
    await refreshAll()
  } catch (e) {
    alert(e instanceof Error ? e.message : String(e))
  }
}

// ---------------------------------------------------------------------------
// 포매팅 헬퍼
// ---------------------------------------------------------------------------

function formatDate(iso: string): string {
  return iso.replace('T', ' ').slice(0, 16)
}

// LocalDateTime ↔ <input type="datetime-local"> 변환
function toLocalInput(iso: string): string {
  return iso.slice(0, 16)
}

function toServerIso(local: string): string {
  return local.length === 16 ? `${local}:00` : local
}

const statusBadgeClass: Record<EventResponse['status'], string> = {
  BEFORE: 'bg-amber-100 text-amber-800',
  ONGOING: 'bg-emerald-100 text-emerald-800',
  ENDED: 'bg-gray-200 text-gray-700',
}
</script>

<template>
  <div>
    <header class="mb-6 flex flex-wrap items-end justify-between gap-4">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">이벤트 관리</h1>
        <p class="mt-1 text-sm text-gray-500">이벤트 도메인</p>
      </div>
      <div class="flex flex-wrap items-center gap-2">
        <div class="inline-flex rounded-lg border border-gray-300 bg-white p-0.5 text-sm">
          <button
              type="button"
              class="rounded-md px-3 py-1.5"
              :class="viewMode === 'manage' ? 'bg-gray-900 text-white' : 'text-gray-700'"
              @click="viewMode = 'manage'"
          >관리
          </button>
          <button
              type="button"
              class="rounded-md px-3 py-1.5"
              :class="viewMode === 'raw' ? 'bg-gray-900 text-white' : 'text-gray-700'"
              @click="viewMode = 'raw'"
          >테이블
          </button>
        </div>
        <button
            type="button"
            class="inline-flex items-center rounded-lg border border-gray-300 bg-white px-3.5 py-2 text-sm font-medium text-gray-700 shadow-sm transition hover:bg-gray-50"
            :disabled="pendingTrees"
            @click="refreshAll()"
        >새로고침
        </button>
        <button
            type="button"
            class="inline-flex items-center rounded-lg bg-brand-600 px-3.5 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-brand-700"
            @click="openCreateParent"
        >+ 신규 이벤트
        </button>
      </div>
    </header>

    <!-- 관리 뷰 -->
    <section v-if="viewMode === 'manage'" class="space-y-4">
      <div v-if="pendingTrees" class="rounded-xl border border-gray-200 bg-white px-6 py-16 text-center">
        <p class="text-sm font-medium text-gray-900">불러오는 중...</p>
      </div>

      <div v-else-if="errorTrees" class="rounded-xl border border-red-200 bg-red-50 px-6 py-16 text-center">
        <p class="text-sm font-medium text-red-700">데이터를 불러오지 못했습니다</p>
        <p class="mt-1 text-sm text-red-600">{{ errorTrees.message }}</p>
      </div>

      <div
          v-else-if="!trees || trees.length === 0"
          class="rounded-xl border border-gray-200 bg-white px-6 py-16 text-center"
      >
        <p class="text-sm font-medium text-gray-900">등록된 이벤트가 없습니다</p>
        <p class="mt-1 text-sm text-gray-500">신규 이벤트를 추가해 보세요.</p>
      </div>

      <article
          v-for="tree in trees"
          v-else
          :key="tree.parent.eventId"
          class="rounded-xl border border-gray-200 bg-white"
      >
        <header class="flex flex-wrap items-start justify-between gap-3 border-b border-gray-100 px-5 py-4">
          <div class="min-w-0">
            <div class="flex flex-wrap items-center gap-2">
              <h2 class="text-base font-semibold text-gray-900">{{ tree.parent.name }}</h2>
              <span class="rounded-full bg-gray-100 px-2 py-0.5 text-xs font-medium text-gray-700">
                {{ tree.parent.type }}
              </span>
              <span
                  v-if="statusById.get(tree.parent.eventId)"
                  class="rounded-full px-2 py-0.5 text-xs font-medium"
                  :class="statusBadgeClass[statusById.get(tree.parent.eventId)!]"
              >
                {{ statusById.get(tree.parent.eventId) }}
              </span>
              <span
                  class="rounded-full px-2 py-0.5 text-xs font-medium"
                  :class="tree.parent.isActive ? 'bg-emerald-50 text-emerald-700' : 'bg-gray-100 text-gray-500'"
              >
                {{ tree.parent.isActive ? 'active' : 'inactive' }}
              </span>
            </div>
            <p class="mt-1 text-xs text-gray-500">
              #{{ tree.parent.eventId }} · {{ formatDate(tree.parent.startedAt) }} ~ {{ formatDate(tree.parent.endedAt) }}
            </p>
          </div>
          <div class="flex flex-wrap items-center gap-2">
            <button
                type="button"
                class="rounded-md border border-gray-300 bg-white px-2.5 py-1 text-xs font-medium text-gray-700 hover:bg-gray-50"
                @click="openCreateChild(tree.parent)"
            >+ 자식 추가
            </button>
            <button
                type="button"
                class="rounded-md border border-gray-300 bg-white px-2.5 py-1 text-xs font-medium text-gray-700 hover:bg-gray-50"
                @click="openUpdateParent(tree.parent)"
            >수정
            </button>
            <button
                type="button"
                class="rounded-md border border-red-200 bg-white px-2.5 py-1 text-xs font-medium text-red-600 hover:bg-red-50"
                @click="onDeleteParent(tree.parent)"
            >삭제
            </button>
          </div>
        </header>

        <div v-if="tree.children.length === 0" class="px-5 py-6 text-center text-sm text-gray-500">
          자식 항목이 없습니다.
        </div>

        <ul v-else class="divide-y divide-gray-100">
          <li
              v-for="child in tree.children"
              :key="child.eventId"
              class="flex flex-wrap items-center justify-between gap-3 px-5 py-3"
          >
            <div class="min-w-0">
              <p class="text-sm font-medium text-gray-900">
                <span v-if="tree.parent.type === 'ATTENDANCE'" class="mr-2 text-xs text-gray-500">
                  D{{ child.day }}
                </span>
                {{ child.name }}
              </p>
              <p class="mt-0.5 text-xs text-gray-500">
                #{{ child.eventId }} · 보상 {{ child.reward }}
                <span v-if="child.completionCondition"> · 조건 {{ child.completionCondition }}</span>
              </p>
            </div>
            <div class="flex items-center gap-2">
              <button
                  type="button"
                  class="rounded-md border border-gray-300 bg-white px-2.5 py-1 text-xs font-medium text-gray-700 hover:bg-gray-50"
                  @click="openUpdateChild(tree.parent, child)"
              >수정
              </button>
              <button
                  type="button"
                  class="rounded-md border border-red-200 bg-white px-2.5 py-1 text-xs font-medium text-red-600 hover:bg-red-50"
                  @click="onDeleteChild(child)"
              >삭제
              </button>
            </div>
          </li>
        </ul>
      </article>
    </section>

    <!-- 원본 테이블 뷰 (eventMaster 한 행의 모든 컬럼) -->
    <section v-else class="rounded-xl border border-gray-200 bg-white">
      <div v-if="flatRows.length === 0" class="px-6 py-16 text-center">
        <p class="text-sm text-gray-500">표시할 행이 없습니다.</p>
      </div>
      <div v-else class="overflow-x-auto">
        <table class="w-full text-xs">
          <thead class="bg-gray-50 text-left text-gray-600">
          <tr>
            <th
                v-for="col in rawColumns"
                :key="col"
                class="whitespace-nowrap border-b border-gray-200 px-3 py-2 font-medium"
            >
              {{ col }}
            </th>
          </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
          <tr
              v-for="row in flatRows"
              :key="row.eventId"
              :class="row.isParentRow ? 'bg-white font-medium text-gray-900' : 'bg-gray-50/60 text-gray-700'"
          >
            <td
                v-for="col in rawColumns"
                :key="col"
                class="whitespace-nowrap px-3 py-2 align-top"
                :class="{ 'text-gray-400': row[col] === null }"
            >
              <template v-if="row[col] === null">NULL</template>
              <template v-else-if="typeof row[col] === 'boolean'">{{ row[col] ? 'true' : 'false' }}</template>
              <template v-else>{{ row[col] }}</template>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- 모달 -->
    <!-- ============================================================ -->
    <div
        v-if="modal.kind !== 'none'"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4"
        @click.self="closeModal"
    >
      <div class="w-full max-w-lg rounded-xl bg-white shadow-xl">
        <!-- 부모 생성 / 수정 (생성 모드에서만 자식 항목 섹션 노출) -->
        <form
            v-if="modal.kind === 'createParent' || modal.kind === 'updateParent'"
            @submit.prevent="modal.kind === 'createParent' ? submitCreateParent() : submitUpdateParent()"
        >
          <header class="border-b border-gray-100 px-5 py-4">
            <h3 class="text-base font-semibold text-gray-900">
              {{ modal.kind === 'createParent' ? '신규 이벤트' : '이벤트 수정' }}
            </h3>
          </header>
          <div class="max-h-[70vh] space-y-4 overflow-y-auto px-5 py-4">
            <div v-if="modal.kind === 'createParent'">
              <label class="block text-xs font-medium text-gray-700">타입</label>
              <select v-model="parentForm.type"
                      class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm">
                <option value="MISSION">MISSION</option>
                <option value="ATTENDANCE">ATTENDANCE</option>
              </select>
            </div>
            <div>
              <label class="block text-xs font-medium text-gray-700">이름</label>
              <input v-model="parentForm.name" type="text" required
                     class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"/>
            </div>
            <div class="flex items-center gap-2">
              <input id="parent-active" v-model="parentForm.isActive" type="checkbox"/>
              <label for="parent-active" class="text-sm text-gray-700">활성화</label>
            </div>
            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-xs font-medium text-gray-700">시작</label>
                <input v-model="parentForm.startedAt" type="datetime-local" required
                       class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"/>
              </div>
              <div>
                <label class="block text-xs font-medium text-gray-700">종료</label>
                <input v-model="parentForm.endedAt" type="datetime-local" required
                       class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"/>
              </div>
            </div>

            <!-- 자식 항목: 생성 모드에서만 -->
            <div v-if="modal.kind === 'createParent'" class="border-t border-gray-100 pt-4">
              <div class="mb-2 flex items-center justify-between">
                <p class="text-sm font-medium text-gray-900">
                  자식 항목 <span class="text-xs font-normal text-gray-500">(선택)</span>
                </p>
                <button type="button"
                        class="rounded-md border border-gray-300 bg-white px-2.5 py-1 text-xs font-medium text-gray-700 hover:bg-gray-50"
                        @click="addDraftItem">+ 항목 추가
                </button>
              </div>
              <p v-if="parentForm.items.length === 0" class="text-xs text-gray-500">
                항목을 추가하지 않으면 부모 이벤트만 생성됩니다.
              </p>
              <div
                  v-for="(item, idx) in parentForm.items"
                  :key="idx"
                  class="mb-2 rounded-lg border border-gray-200 p-3"
              >
                <div class="mb-2 flex items-center justify-between">
                  <p class="text-xs font-medium text-gray-700">#{{ idx + 1 }}</p>
                  <button type="button" class="text-xs text-red-600 hover:underline" @click="removeDraftItem(idx)">
                    제거
                  </button>
                </div>
                <div class="grid grid-cols-2 gap-2">
                  <input v-model="item.name" type="text" placeholder="이름" required
                         class="rounded-md border border-gray-300 px-2 py-1.5 text-sm"/>
                  <select v-model="item.reward" class="rounded-md border border-gray-300 px-2 py-1.5 text-sm">
                    <option value="POINT">POINT</option>
                    <option value="ITEM">ITEM</option>
                  </select>
                  <select v-if="parentForm.type === 'MISSION'" v-model="item.completionCondition"
                          class="rounded-md border border-gray-300 px-2 py-1.5 text-sm">
                    <option value="CLICK">CLICK</option>
                    <option value="PRE_REGISTER">PRE_REGISTER</option>
                  </select>
                  <input v-if="parentForm.type === 'ATTENDANCE'" v-model.number="item.day" type="number" min="1"
                         placeholder="day" required
                         class="rounded-md border border-gray-300 px-2 py-1.5 text-sm"/>
                </div>
              </div>
            </div>

            <p v-if="modalError" class="text-sm text-red-600">{{ modalError }}</p>
          </div>
          <footer class="flex justify-end gap-2 border-t border-gray-100 px-5 py-3">
            <button type="button" class="rounded-md border border-gray-300 px-3 py-1.5 text-sm" @click="closeModal">
              취소
            </button>
            <button type="submit" class="rounded-md bg-brand-600 px-3 py-1.5 text-sm text-white"
                    :disabled="submitting">
              {{ submitting ? '저장 중...' : '저장' }}
            </button>
          </footer>
        </form>

        <!-- 자식 생성 / 수정 -->
        <form
            v-else-if="modal.kind === 'createChild' || modal.kind === 'updateChild'"
            @submit.prevent="modal.kind === 'createChild' ? submitCreateChild() : submitUpdateChild()"
        >
          <header class="border-b border-gray-100 px-5 py-4">
            <h3 class="text-base font-semibold text-gray-900">
              {{ modal.kind === 'createChild' ? '자식 추가' : '자식 수정' }}
              <span class="ml-2 text-xs font-normal text-gray-500">({{ modal.parentType }})</span>
            </h3>
          </header>
          <div class="space-y-4 px-5 py-4">
            <div>
              <label class="block text-xs font-medium text-gray-700">이름</label>
              <input v-model="childForm.name" type="text" required
                     class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"/>
            </div>
            <div>
              <label class="block text-xs font-medium text-gray-700">보상</label>
              <select v-model="childForm.reward"
                      class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm">
                <option value="POINT">POINT</option>
                <option value="ITEM">ITEM</option>
              </select>
            </div>
            <div v-if="modal.parentType === 'MISSION'">
              <label class="block text-xs font-medium text-gray-700">완료 조건</label>
              <select v-model="childForm.completionCondition"
                      class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm">
                <option value="CLICK">CLICK</option>
                <option value="PRE_REGISTER">PRE_REGISTER</option>
              </select>
            </div>
            <div v-if="modal.parentType === 'ATTENDANCE'">
              <label class="block text-xs font-medium text-gray-700">일차 (day)</label>
              <input v-model.number="childForm.day" type="number" min="1" required
                     class="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"/>
            </div>
            <p v-if="modalError" class="text-sm text-red-600">{{ modalError }}</p>
          </div>
          <footer class="flex justify-end gap-2 border-t border-gray-100 px-5 py-3">
            <button type="button" class="rounded-md border border-gray-300 px-3 py-1.5 text-sm" @click="closeModal">
              취소
            </button>
            <button type="submit" class="rounded-md bg-brand-600 px-3 py-1.5 text-sm text-white"
                    :disabled="submitting">
              {{ submitting ? '저장 중...' : '저장' }}
            </button>
          </footer>
        </form>
      </div>
    </div>
  </div>
</template>
