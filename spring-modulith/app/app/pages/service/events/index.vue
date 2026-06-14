<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import type { EventResponse } from '~/api/event'
import type { EventStatus } from '~/api/types'

definePageMeta({ layout: 'service' })

const { $api } = useNuxtApp()

// ---------------------------------------------------------------------------
// userId 관리 (localStorage 보관)
// ---------------------------------------------------------------------------

const USER_ID_STORAGE_KEY = 'service.userId'
const userIdInput = ref<string>('')

onMounted(() => {
  userIdInput.value = localStorage.getItem(USER_ID_STORAGE_KEY) ?? ''
})

const userId = computed<number | null>(() => {
  const n = Number(userIdInput.value)
  return Number.isInteger(n) && n > 0 ? n : null
})

function saveUserId(): void {
  if (!userId.value) {
    alert('1 이상의 정수를 입력하세요.')
    return
  }
  localStorage.setItem(USER_ID_STORAGE_KEY, String(userId.value))
}

// ---------------------------------------------------------------------------
// 상태 필터
// ---------------------------------------------------------------------------

const filters = reactive<Record<EventStatus, boolean>>({
  ONGOING: true,
  BEFORE: false,
  ENDED: false,
})

const activeStatuses = computed<EventStatus[]>(() => {
  const all: EventStatus[] = ['ONGOING', 'BEFORE', 'ENDED']
  const selected = all.filter(s => filters[s])
  return selected.length > 0 ? selected : ['ONGOING']
})

const statusLabel: Record<EventStatus, string> = {
  ONGOING: '진행중',
  BEFORE: '시작 전',
  ENDED: '종료',
}

const statusBadgeClass: Record<EventStatus, string> = {
  BEFORE: 'bg-amber-100 text-amber-800',
  ONGOING: 'bg-emerald-100 text-emerald-800',
  ENDED: 'bg-gray-200 text-gray-700',
}

// ---------------------------------------------------------------------------
// 데이터 로딩 — activeStatuses 변경 시 자동 refetch
// ---------------------------------------------------------------------------

const { data: events, pending, error, refresh } = await useAsyncData<EventResponse[]>(
    'service-events',
    () => $api<EventResponse[]>('/api/events', { query: { statuses: activeStatuses.value } }),
    {
      watch: [activeStatuses],
      default: () => [] as EventResponse[],
    },
)

// ---------------------------------------------------------------------------
// 참여(완료) 핸들러 — 자식 항목(MissionItem / AttendanceItem) 단위
// ---------------------------------------------------------------------------

const submittingIds = reactive(new Set<number>())

async function onParticipate(itemId: number): Promise<void> {
  if (!userId.value) {
    alert('상단에 내 ID 를 먼저 입력하세요.')
    return
  }
  if (submittingIds.has(itemId)) return
  submittingIds.add(itemId)
  try {
    await $api(`/api/events/${itemId}/complete`, {
      method: 'POST',
      headers: { 'X-User-Id': String(userId.value) },
    })
    alert('참여 완료')
    await refresh()
  } catch (e) {
    alert(e instanceof Error ? e.message : String(e))
  } finally {
    submittingIds.delete(itemId)
  }
}

function isParticipatable(event: EventResponse): boolean {
  return !!userId.value && event.status === 'ONGOING'
}

function formatDate(iso: string): string {
  return iso.replace('T', ' ').slice(0, 16)
}
</script>

<template>
  <div>
    <header class="mb-6">
      <h1 class="text-2xl font-semibold tracking-tight text-gray-900">이벤트</h1>
      <p class="mt-1 text-sm text-gray-500">참여 가능한 이벤트 목록 · 자식 항목 단위로 참여합니다.</p>
    </header>

    <!-- 컨트롤 바: 내 ID + 상태 필터 + 새로고침 -->
    <section class="mb-4 flex flex-wrap items-end justify-between gap-3 rounded-xl border border-gray-200 bg-white px-4 py-3">
      <div class="flex flex-wrap items-end gap-3">
        <div>
          <label class="block text-xs font-medium text-gray-700">내 ID</label>
          <div class="mt-1 flex items-center gap-2">
            <input
                v-model="userIdInput"
                type="number"
                min="1"
                placeholder="예: 12345"
                class="w-40 rounded-md border border-gray-300 px-3 py-1.5 text-sm"
            />
            <button
                type="button"
                class="rounded-md border border-gray-300 bg-white px-3 py-1.5 text-sm font-medium text-gray-700 hover:bg-gray-50"
                @click="saveUserId"
            >
              저장
            </button>
          </div>
          <p v-if="!userId" class="mt-1 text-xs text-amber-600">참여하려면 ID 가 필요합니다.</p>
        </div>

        <div>
          <label class="block text-xs font-medium text-gray-700">상태 필터</label>
          <div class="mt-1 flex items-center gap-3 rounded-md border border-gray-300 px-3 py-1.5">
            <label
                v-for="s in (['ONGOING','BEFORE','ENDED'] as const)"
                :key="s"
                class="flex items-center gap-1.5 text-sm text-gray-700"
            >
              <input v-model="filters[s]" type="checkbox" />
              <span>{{ statusLabel[s] }}</span>
            </label>
          </div>
        </div>
      </div>

      <button
          type="button"
          class="rounded-md border border-gray-300 bg-white px-3 py-1.5 text-sm font-medium text-gray-700 hover:bg-gray-50"
          :disabled="pending"
          @click="refresh()"
      >
        새로고침
      </button>
    </section>

    <!-- 로딩 / 에러 / 빈 상태 -->
    <div v-if="pending" class="rounded-xl border border-gray-200 bg-white px-6 py-16 text-center">
      <p class="text-sm font-medium text-gray-900">불러오는 중...</p>
    </div>

    <div v-else-if="error" class="rounded-xl border border-red-200 bg-red-50 px-6 py-16 text-center">
      <p class="text-sm font-medium text-red-700">데이터를 불러오지 못했습니다</p>
      <p class="mt-1 text-sm text-red-600">{{ error?.message ?? String(error) }}</p>
    </div>

    <div
        v-else-if="!events || events.length === 0"
        class="rounded-xl border border-dashed border-gray-300 bg-white px-6 py-16 text-center"
    >
      <p class="text-sm text-gray-500">조건에 맞는 이벤트가 없습니다.</p>
    </div>

    <!-- 이벤트 목록 -->
    <ul v-else class="grid grid-cols-1 gap-4 lg:grid-cols-2">
      <li
          v-for="event in events"
          :key="event.id"
          class="rounded-xl border border-gray-200 bg-white"
      >
        <header class="flex flex-wrap items-start justify-between gap-3 border-b border-gray-100 px-5 py-4">
          <div class="min-w-0">
            <div class="flex flex-wrap items-center gap-2">
              <p class="text-base font-semibold text-gray-900">{{ event.name }}</p>
              <span class="rounded-full bg-gray-100 px-2 py-0.5 text-xs font-medium text-gray-700">
                {{ event.type }}
              </span>
              <span
                  class="rounded-full px-2 py-0.5 text-xs font-medium"
                  :class="statusBadgeClass[event.status]"
              >
                {{ statusLabel[event.status] }}
              </span>
            </div>
            <p class="mt-1 text-xs text-gray-500">
              {{ formatDate(event.startedAt) }} ~ {{ formatDate(event.endedAt) }}
            </p>
          </div>
        </header>

        <!-- MISSION: 미션 항목 리스트 -->
        <ul
            v-if="event.missions && event.missions.length > 0"
            class="divide-y divide-gray-100"
        >
          <li
              v-for="m in event.missions"
              :key="m.id"
              class="flex flex-wrap items-center justify-between gap-3 px-5 py-3"
          >
            <div class="min-w-0">
              <p class="text-sm font-medium text-gray-900">{{ m.name }}</p>
              <p class="mt-0.5 text-xs text-gray-500">
                보상 {{ m.reward }} · 조건 {{ m.completionCondition }}
              </p>
            </div>
            <button
                type="button"
                class="rounded-md bg-brand-600 px-3 py-1.5 text-xs font-medium text-white shadow-sm transition hover:bg-brand-700 disabled:cursor-not-allowed disabled:bg-gray-300"
                :disabled="!isParticipatable(event) || submittingIds.has(m.id)"
                :title="!userId ? '내 ID 를 입력하세요' : (event.status !== 'ONGOING' ? '진행중인 이벤트만 참여 가능' : '')"
                @click="onParticipate(m.id)"
            >
              {{ submittingIds.has(m.id) ? '...' : '참여하기' }}
            </button>
          </li>
        </ul>

        <!-- ATTENDANCE: 출석 일자 리스트 -->
        <ul
            v-else-if="event.attendances && event.attendances.length > 0"
            class="divide-y divide-gray-100"
        >
          <li
              v-for="a in event.attendances"
              :key="a.id"
              class="flex flex-wrap items-center justify-between gap-3 px-5 py-3"
          >
            <div class="min-w-0">
              <p class="text-sm font-medium text-gray-900">D{{ a.day }}</p>
              <p class="mt-0.5 text-xs text-gray-500">보상 {{ a.reward }}</p>
            </div>
            <button
                type="button"
                class="rounded-md bg-brand-600 px-3 py-1.5 text-xs font-medium text-white shadow-sm transition hover:bg-brand-700 disabled:cursor-not-allowed disabled:bg-gray-300"
                :disabled="!isParticipatable(event) || submittingIds.has(a.id)"
                :title="!userId ? '내 ID 를 입력하세요' : (event.status !== 'ONGOING' ? '진행중인 이벤트만 참여 가능' : '')"
                @click="onParticipate(a.id)"
            >
              {{ submittingIds.has(a.id) ? '...' : '출석' }}
            </button>
          </li>
        </ul>

        <div v-else class="px-5 py-6 text-center text-sm text-gray-500">
          참여 가능한 항목이 없습니다.
        </div>
      </li>
    </ul>
  </div>
</template>
