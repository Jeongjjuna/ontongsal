<script setup lang="ts">
import { computed } from 'vue'
import type { EventResponse } from '~/api/event'

definePageMeta({layout: 'admin'})

const { $api } = useNuxtApp()

const { data: events, pending, error, refresh } = await useAsyncData<EventResponse[]>(
    'admin-dashboard-events',
    () => $api<EventResponse[]>('/api/admin/events'),
    { default: () => [] as EventResponse[] },
)

const cards = computed(() => {
  const raw = events.value
  const list = Array.isArray(raw) ? raw : []
  return [
    { label: '전체 이벤트', value: list.length },
    { label: '진행 중', value: list.filter(e => e.status === 'ONGOING').length },
    { label: '미션 이벤트', value: list.filter(e => e.type === 'MISSION').length },
    { label: '출석 이벤트', value: list.filter(e => e.type === 'ATTENDANCE').length },
  ]
})
</script>

<template>
  <div>
    <header class="mb-8 flex items-end justify-between gap-4">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">대시보드</h1>
        <p class="mt-1 text-sm text-gray-500">이벤트 도메인 현황을 한 눈에 봅니다.</p>
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

    <div v-if="error" class="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
      데이터를 불러오지 못했습니다: {{ error?.message ?? error?.statusMessage ?? String(error) }}
    </div>

    <section class="grid grid-cols-2 gap-4 sm:grid-cols-4">
      <div
          v-for="c in cards"
          :key="c.label"
          class="rounded-xl border border-gray-200 bg-white p-5"
      >
        <p class="text-sm text-gray-500">{{ c.label }}</p>
        <p class="mt-2 text-2xl font-semibold tracking-tight text-gray-900">
          {{ pending ? '...' : c.value }}
        </p>
      </div>
    </section>
  </div>
</template>
