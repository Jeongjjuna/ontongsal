<template>
  <div class="flex min-h-screen items-center justify-center bg-gray-50 px-6">
    <div class="w-full max-w-3xl">
      <div class="mb-10 text-center">
        <p class="text-sm font-medium text-brand-600">ontongsal</p>
        <h1 class="mt-2 text-3xl font-semibold tracking-tight text-gray-900">
          어디로 이동할까요?
        </h1>
        <p class="mt-2 text-sm text-gray-500">
          고객용 화면과 관리자 화면이 분리되어 있습니다.
        </p>
      </div>

      <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <NuxtLink
          to="/service"
          class="group rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:border-brand-500 hover:shadow-md"
        >
          <div class="mb-4 inline-flex h-10 w-10 items-center justify-center rounded-lg bg-brand-50 text-brand-600">
            S
          </div>
          <h2 class="text-lg font-semibold text-gray-900">Service</h2>
          <p class="mt-1 text-sm text-gray-500">고객용 화면 — 이벤트, 주문</p>
          <span class="mt-4 inline-flex items-center text-sm font-medium text-brand-600 group-hover:underline">
            바로가기 →
          </span>
        </NuxtLink>

        <NuxtLink
          to="/admin"
          class="group rounded-2xl border border-gray-800 bg-gray-900 p-6 text-white shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
        >
          <div class="mb-4 inline-flex h-10 w-10 items-center justify-center rounded-lg bg-white/10 text-white">
            A
          </div>
          <h2 class="text-lg font-semibold">Admin</h2>
          <p class="mt-1 text-sm text-gray-400">관리자 화면 — 이벤트/상품/재고/주문</p>
          <span class="mt-4 inline-flex items-center text-sm font-medium text-white group-hover:underline">
            바로가기 →
          </span>
        </NuxtLink>
      </div>
    </div>
  </div>
</template>
