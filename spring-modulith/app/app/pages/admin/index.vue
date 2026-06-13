<script setup lang="ts">
definePageMeta({layout: 'admin'})

const cards = [
  {label: '이벤트', value: '-'},
  {label: '상품', value: '-'},
  {label: '재고', value: '-'},
  {label: '주문', value: '-'},
]
</script>

<template>
  <div>
    <header class="mb-8">
      <h1 class="text-2xl font-semibold tracking-tight text-gray-900">대시보드</h1>
      <p class="mt-1 text-sm text-gray-500">전체 리소스 현황을 한 눈에 봅니다.</p>
    </header>

    <section class="grid grid-cols-2 gap-4 sm:grid-cols-4">
      <div
        v-for="c in cards"
        :key="c.label"
        class="rounded-xl border border-gray-200 bg-white p-5"
      >
        <p class="text-sm text-gray-500">{{ c.label }}</p>
        <p class="mt-2 text-2xl font-semibold tracking-tight text-gray-900">{{ c.value }}</p>
      </div>
    </section>
  </div>
</template>
