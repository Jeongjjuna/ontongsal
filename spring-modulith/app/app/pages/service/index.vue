<script setup lang="ts">
import type { EventResponse } from '~/api/event'

definePageMeta({layout: 'service'})

const { $api } = useNuxtApp()

const { data: events, pending, error, refresh } = await useAsyncData<EventResponse[]>(
    'service-ongoing-events',
    () => $api<EventResponse[]>('/api/events', { query: { statuses: ['ONGOING'] } }),
    { default: () => [] as EventResponse[] },
)

function formatDate(iso: string): string {
  return iso.replace('T', ' ').slice(0, 16)
}
</script>

<template>
  <div>
    <header class="mb-8 flex items-end justify-between gap-4">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">진행 중 이벤트</h1>
        <p class="mt-1 text-sm text-gray-500">현재 참여 가능한 이벤트 목록입니다.</p>
      </div>
      <button
          type="button"
          class="inline-flex items-center rounded-lg border border-gray-300 bg-white px-3.5 py-2 text-sm font-medium text-gray-700 shadow-sm transition hover:bg-gray-50"
          :disabled="pending"
          @click="refresh()"
      >
        새로고침
      </button>
    </header>

    <div v-if="pending" class="rounded-xl border border-gray-200 bg-white px-6 py-16 text-center">
      <p class="text-sm font-medium text-gray-900">불러오는 중...</p>
    </div>

    <div
        v-else-if="error"
        class="rounded-xl border border-red-200 bg-red-50 px-6 py-16 text-center"
    >
      <p class="text-sm font-medium text-red-700">데이터를 불러오지 못했습니다</p>
      <p class="mt-1 text-sm text-red-600">{{ error.message }}</p>
    </div>

    <div
        v-else-if="!events || events.length === 0"
        class="rounded-xl border border-dashed border-gray-300 bg-white px-6 py-16 text-center"
    >
      <p class="text-sm text-gray-500">진행 중인 이벤트가 없습니다.</p>
    </div>

    <ul v-else class="grid grid-cols-1 gap-4 sm:grid-cols-2">
      <li
          v-for="event in events"
          :key="event.id"
          class="rounded-xl border border-gray-200 bg-white p-5"
      >
        <div class="flex items-start justify-between gap-3">
          <div>
            <p class="text-base font-semibold text-gray-900">{{ event.name }}</p>
            <p class="mt-1 text-xs text-gray-500">
              {{ formatDate(event.startedAt) }} ~ {{ formatDate(event.endedAt) }}
            </p>
          </div>
          <span
              class="inline-flex items-center rounded-full bg-gray-100 px-2 py-0.5 text-xs font-medium text-gray-700"
          >
            {{ event.type }}
          </span>
        </div>

        <div v-if="event.missions && event.missions.length" class="mt-4 space-y-1.5">
          <p class="text-xs font-medium text-gray-500">미션</p>
          <ul class="space-y-1">
            <li
                v-for="m in event.missions"
                :key="m.id"
                class="flex items-center justify-between text-sm text-gray-700"
            >
              <span>{{ m.name }}</span>
              <span class="text-xs text-gray-500">{{ m.completionCondition }} · {{ m.reward }}</span>
            </li>
          </ul>
        </div>

        <div v-if="event.attendances && event.attendances.length" class="mt-4 space-y-1.5">
          <p class="text-xs font-medium text-gray-500">출석</p>
          <ul class="grid grid-cols-7 gap-1 text-center">
            <li
                v-for="a in event.attendances"
                :key="a.id"
                class="rounded-md bg-gray-50 px-1 py-1.5 text-xs text-gray-700"
            >
              D{{ a.day }}
            </li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
</template>
