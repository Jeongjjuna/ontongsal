<script setup lang="ts">
definePageMeta({layout: 'admin'})
</script>

<template>
  <div>
    <header class="mb-6 flex items-end justify-between gap-4">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">이벤트 관리</h1>
        <p class="mt-1 text-sm text-gray-500">이벤트 도메인</p>
      </div>
      <button
          type="button"
          class="inline-flex items-center rounded-lg bg-brand-600 px-3.5 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-brand-700"
      >
        + 신규 이벤트
      </button>
    </header>

    <section class="rounded-xl border border-gray-200 bg-white">
      <div class="px-6 py-16 text-center">
        <p class="text-sm font-medium text-gray-900">등록된 이벤트가 없습니다</p>
        <p class="mt-1 text-sm text-gray-500">신규 이벤트를 추가해 보세요.</p>
      </div>
    </section>
  </div>
</template>
