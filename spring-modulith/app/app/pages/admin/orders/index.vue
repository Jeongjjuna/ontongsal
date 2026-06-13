<script setup lang="ts">
definePageMeta({layout: 'admin'})
</script>

<template>
  <div>
    <header class="mb-6 flex items-end justify-between gap-4">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">주문 관리</h1>
        <p class="mt-1 text-sm text-gray-500">주문 도메인</p>
      </div>
      <button
        type="button"
        class="inline-flex items-center rounded-lg border border-gray-300 bg-white px-3.5 py-2 text-sm font-medium text-gray-700 shadow-sm transition hover:bg-gray-50"
      >
        필터
      </button>
    </header>

    <section class="rounded-xl border border-gray-200 bg-white">
      <div class="px-6 py-16 text-center">
        <p class="text-sm font-medium text-gray-900">조회된 주문이 없습니다</p>
        <p class="mt-1 text-sm text-gray-500">새로운 주문이 들어오면 이곳에 표시됩니다.</p>
      </div>
    </section>
  </div>
</template>
