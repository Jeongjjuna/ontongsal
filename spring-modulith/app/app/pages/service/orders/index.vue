<script setup lang="ts">
definePageMeta({layout: 'service'})
</script>

<template>
  <div>
    <header class="mb-6 flex items-end justify-between gap-4">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">주문</h1>
        <p class="mt-1 text-sm text-gray-500">주문 도메인</p>
      </div>
    </header>

    <section class="rounded-xl border border-gray-200 bg-white">
      <div class="px-6 py-16 text-center">
        <div class="mx-auto mb-4 inline-flex h-10 w-10 items-center justify-center rounded-full bg-gray-100 text-gray-400">
          ·
        </div>
        <p class="text-sm font-medium text-gray-900">아직 주문 내역이 없습니다</p>
        <p class="mt-1 text-sm text-gray-500">주문이 생성되면 이곳에 표시됩니다.</p>
      </div>
    </section>
  </div>
</template>
