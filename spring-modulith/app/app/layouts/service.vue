<script setup lang="ts">
const menu = [
  {to: '/service/events', label: '이벤트'},
  {to: '/service/orders', label: '주문'},
]
</script>

<template>
  <div class="flex min-h-screen bg-gray-50 text-gray-900">
    <aside class="flex w-64 shrink-0 flex-col border-r border-gray-200 bg-white">
      <div class="flex h-16 items-center px-6 border-b border-gray-200">
        <NuxtLink to="/service" class="flex items-center gap-2">
          <span class="inline-block h-6 w-6 rounded-md bg-brand-500"></span>
          <span class="text-base font-semibold tracking-tight">Service</span>
        </NuxtLink>
      </div>

      <nav class="flex-1 px-3 py-4 space-y-1">
        <NuxtLink
          v-for="item in menu"
          :key="item.to"
          :to="item.to"
          class="block rounded-md px-3 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100"
          active-class="!bg-brand-50 !text-brand-700"
        >
          {{ item.label }}
        </NuxtLink>
      </nav>

      <div class="border-t border-gray-200 p-3">
        <NuxtLink
          to="/admin"
          class="flex items-center justify-between rounded-md px-3 py-2 text-sm text-gray-600 transition hover:bg-gray-100 hover:text-gray-900"
        >
          <span class="flex items-center gap-2">
            <span class="inline-block h-2 w-2 rounded-full bg-gray-900"></span>
            Admin으로 이동
          </span>
          <span aria-hidden="true">→</span>
        </NuxtLink>
      </div>
    </aside>

    <main class="flex-1 px-10 py-8">
      <slot />
    </main>
  </div>
</template>
