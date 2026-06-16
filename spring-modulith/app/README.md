# ontongsal Frontend (Nuxt 4)

ontongsal 백엔드(Spring Modulith)와 통신하는 어드민/서비스 UI 프로젝트입니다.
이 문서는 신규 입사자가 처음 봐도 디렉토리 구조를 빠르게 파악할 수 있도록 정리한 가이드입니다.

> 본 프로젝트는 [Nuxt 4.x 공식 디렉토리 구조](https://nuxt.com/docs/4.x/directory-structure)를 기준으로 합니다.
> 공식 컨벤션과 다른 부분에는 **(우리 프로젝트 추가/변경)** 으로 표시했습니다.

---

## 1. 스택 한눈에 보기

| 영역 | 사용 기술 |
| --- | --- |
| 프레임워크 | Nuxt `^4.4.8` (Vue 3.5) |
| 라우팅 | Nuxt 파일 기반 라우팅 + `vue-router` |
| 상태 관리 | Pinia (`@pinia/nuxt`) — 모듈 등록만 되어 있고 아직 store 미사용 |
| 스타일 | Tailwind CSS v4 (`@tailwindcss/vite` 플러그인) |
| HTTP 클라이언트 | `$fetch` 기반 커스텀 `$api` 플러그인 |
| 언어 | TypeScript 5.7 |
| 백엔드 | Spring Boot (`http://localhost:8080`) — 같은 레포의 `src/` 모듈 |

---

## 2. Nuxt 4 핵심 컨벤션 (먼저 알고 가기)

Nuxt 4부터 가장 큰 변화는 **`app/` 디렉토리 도입**입니다.

| 개념 | 설명 |
| --- | --- |
| **`app/` (소스 루트)** | 클라이언트/유니버설 코드가 모두 이 안에 들어갑니다. Nuxt 3까지 루트에 흩어져 있던 `pages/`, `layouts/`, `plugins/` 등이 전부 `app/` 하위로 이동됐습니다. |
| **`server/` (분리됨)** | Nitro 서버 라우트/미들웨어 전용. 클라이언트 번들에 포함되지 않습니다. **본 프로젝트는 백엔드가 Spring Boot라 `server/` 디렉토리를 사용하지 않습니다.** |
| **자동 import** | `app/composables/`, `app/utils/`, `app/components/` 안의 export는 import 구문 없이 사용 가능. Vue/Nuxt 내장 함수(`ref`, `useAsyncData`, `useNuxtApp` 등)도 자동 import 됩니다. |
| **파일 기반 라우팅** | `app/pages/admin/events/index.vue` → `/admin/events` 로 자동 매핑. |
| **레이아웃** | `app/layouts/xxx.vue` 작성 후 페이지에서 `definePageMeta({ layout: 'xxx' })` 로 선택. |
| **`~` / `@` 별칭** | 둘 다 `app/` 루트를 가리킵니다. 예: `~/api/event` → `app/api/event.ts`. |

---

## 3. 전체 폴더 트리

```
app/                              ← Nuxt 프로젝트 루트
├── app/                          ← 소스 루트 (Nuxt 4 컨벤션)
│   ├── app.vue                   ← 최상위 진입점 (NuxtLayout + NuxtPage)
│   ├── api/                      ← BE API 계약 타입 정의 (우리 프로젝트 추가/변경)
│   │   ├── types.ts              ← 공용 enum / 도메인 타입
│   │   ├── event.ts              ← /api/events 응답 타입
│   │   ├── eventAdmin.ts         ← /api/admin/events 요청·응답 타입
│   │   ├── inventory.ts          ← /inventories 응답 타입
│   │   ├── item.ts               ← /items 응답 타입
│   │   └── order.ts              ← /orders 요청·응답 타입
│   ├── assets/
│   │   └── css/
│   │       └── main.css          ← Tailwind v4 엔트리 + 테마 토큰
│   ├── layouts/
│   │   ├── default.vue           ← 기본 레이아웃 (단순 래퍼)
│   │   ├── admin.vue             ← 어드민 사이드바 레이아웃
│   │   └── service.vue           ← 고객용 사이드바 레이아웃
│   ├── pages/                    ← 파일 기반 라우팅
│   │   ├── index.vue             ← / (Admin·Service 진입 선택)
│   │   ├── admin/
│   │   │   ├── index.vue         ← /admin (대시보드)
│   │   │   ├── events/index.vue  ← /admin/events
│   │   │   ├── items/index.vue   ← /admin/items
│   │   │   ├── inventories/index.vue
│   │   │   └── orders/index.vue
│   │   └── service/
│   │       ├── index.vue         ← /service (진행 중 이벤트)
│   │       ├── events/index.vue
│   │       └── orders/index.vue
│   └── plugins/
│       └── api.ts                ← $fetch 래퍼: BE 응답 envelope 처리
├── public/                       ← 정적 자산 (그대로 서빙)
│   ├── favicon.ico
│   └── robots.txt
├── .nuxt/                        ← 빌드 산출물 (git ignore)
├── node_modules/                 ← (git ignore)
├── nuxt.config.ts                ← Nuxt 설정
├── package.json
├── tsconfig.json                 ← .nuxt/tsconfig.*.json 들을 참조
├── .gitignore
└── README.md
```

---

## 4. 디렉토리별 역할

### 4.1 루트(`app/`)

| 경로 | 역할 |
| --- | --- |
| `nuxt.config.ts` | Nuxt 설정. Pinia 모듈 등록, Tailwind v4 Vite 플러그인, 글로벌 CSS 지정. |
| `package.json` | 스크립트(`dev`/`build`/`generate`/`preview`)와 의존성. |
| `tsconfig.json` | Nuxt가 생성한 `.nuxt/tsconfig.*.json` 들을 references로 묶음. 직접 컴파일러 옵션을 수정하지 않습니다. |
| `public/` | URL 루트에 그대로 노출되는 정적 파일 (favicon, robots.txt 등). |
| `.nuxt/` | `nuxt prepare`/`dev`/`build`로 생성. 절대 손대지 않습니다. |

### 4.2 소스 루트(`app/app/`)

| 경로 | 역할 | 공식 컨벤션 여부 |
| --- | --- | --- |
| `app.vue` | 최상위 컴포넌트. `<NuxtLayout>`으로 레이아웃 슬롯을 열고 `<NuxtPage>`로 라우트 컴포넌트를 렌더링. | 표준 |
| `assets/` | 빌드 파이프라인을 거치는 자산(CSS, 폰트 등). `main.css`에 Tailwind v4 엔트리(`@import "tailwindcss";`)와 `@theme` 변수(브랜드 컬러, Pretendard 폰트) 정의. | 표준 |
| `layouts/` | 페이지를 감싸는 레이아웃. `<slot />`에 페이지 콘텐츠가 들어감. `definePageMeta({ layout: 'admin' })` 처럼 페이지에서 선택. | 표준 |
| `pages/` | 파일 기반 라우팅. `pages/admin/events/index.vue` → `/admin/events`. | 표준 |
| `plugins/` | Nuxt 앱 초기화 시점에 실행되는 코드. `defineNuxtPlugin` 으로 작성. | 표준 |
| `api/` | **BE API 계약 타입 정의 모음 (우리 프로젝트 추가/변경)**. Nuxt 공식 디렉토리가 아닙니다. 자동 import 대상이 아니므로 `import type { ... } from '~/api/event'` 형태로 명시 import. ⚠️ Nuxt의 `server/api/` 와 혼동 주의 — 여긴 단순 타입 모음입니다. | 우리 프로젝트 추가/변경 |

### 4.3 공식 컨벤션에 있지만 **아직 사용하지 않는** 디렉토리

신규로 추가할 때 참고하세요. 모두 `app/app/` 하위에 만들면 됩니다.

| 경로 | 용도 | 자동 import |
| --- | --- | --- |
| `components/` | 재사용 Vue 컴포넌트 | ✅ |
| `composables/` | `useXxx()` 형태의 컴포저블 | ✅ |
| `utils/` | 순수 헬퍼 함수 | ✅ |
| `middleware/` | 라우트 가드 (전역/페이지별) | 자동 등록 |
| `stores/` | Pinia store (`@pinia/nuxt` 자동 import) | ✅ |
| `shared/` | 클라이언트/서버가 함께 쓰는 타입·유틸 | 별칭 `#shared` |

---

## 5. 주요 파일 상세

### 5.1 `app/app.vue` — 최상위 진입점

```vue
<template>
  <div>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>
```

`NuxtLayout`이 페이지가 선택한 레이아웃을 슬롯에 끼우고, `NuxtPage`가 현재 라우트의 페이지를 렌더링합니다.

### 5.2 `app/plugins/api.ts` — `$api` 글로벌 HTTP 클라이언트

`useNuxtApp().$api` 로 어디서나 호출할 수 있는 `$fetch` 인스턴스를 제공합니다. 핵심 동작:

| 동작 | 설명 |
| --- | --- |
| `baseURL` | `http://localhost:8080` (Spring Boot) |
| **응답 envelope 언래핑** | BE가 `{ code, message, data }` 형태로 응답 → `data` 만 추출해서 호출부에 넘김. |
| **에러 출처 구분** | 응답 헤더 `X-Response-Origin: self` 가 있으면 BE 도달 성공, 아니면 네트워크 에러로 분류. |
| **사용자 메시지 throw** | 4xx → BE message, 5xx → 일반 메시지, 네트워크 단절 → 일시적 오류 메시지로 throw. |

호출 예:

```ts
const { $api } = useNuxtApp()
const events = await $api<EventResponse[]>('/api/events', { query: { statuses: ['ONGOING'] } })
```

### 5.3 `app/api/*.ts` — BE 계약 타입

Spring Boot 컨트롤러의 요청/응답 DTO와 1:1 매핑되는 TypeScript 타입을 모아둔 디렉토리입니다.

| 파일 | 대응 BE 컨트롤러 |
| --- | --- |
| `types.ts` | 공용 enum 미러 (`EventStatus`, `EventType`, `RewardType`, `CompletionCondition`, `IsoDateTime`) |
| `event.ts` | `EventController` — `/api/events` (고객용 조회) |
| `eventAdmin.ts` | `EventAdminController` — `/api/admin/events` (CRUD + 트리/원본 조회) |
| `item.ts` | `ItemController` — `/items` |
| `inventory.ts` | `InventoryController` — `/inventories` |
| `order.ts` | `OrderController` — `/orders` |

> ⚠️ 백엔드 enum을 손으로 미러링하므로 BE 변경 시 반드시 함께 업데이트해야 합니다.

### 5.4 `app/layouts/*` — 레이아웃 3종

| 레이아웃 | 용도 | 적용 방식 |
| --- | --- | --- |
| `default.vue` | 단순 배경 래퍼. 루트 `/` 진입 페이지가 사용. | 기본값 (지정 없을 때) |
| `admin.vue` | 좌측 다크 사이드바 + `Service로 이동` 링크. 어드민 메뉴(이벤트/상품/재고/주문). | `definePageMeta({ layout: 'admin' })` |
| `service.vue` | 좌측 라이트 사이드바 + `Admin으로 이동` 링크. 고객 메뉴(이벤트/주문). | `definePageMeta({ layout: 'service' })` |

### 5.5 `app/pages/*` — 라우팅 매핑

| 파일 경로 | URL | 레이아웃 |
| --- | --- | --- |
| `pages/index.vue` | `/` | default |
| `pages/admin/index.vue` | `/admin` | admin |
| `pages/admin/events/index.vue` | `/admin/events` | admin |
| `pages/admin/items/index.vue` | `/admin/items` | admin |
| `pages/admin/inventories/index.vue` | `/admin/inventories` | admin |
| `pages/admin/orders/index.vue` | `/admin/orders` | admin |
| `pages/service/index.vue` | `/service` | service |
| `pages/service/events/index.vue` | `/service/events` | service |
| `pages/service/orders/index.vue` | `/service/orders` | service |

### 5.6 `app/assets/css/main.css` — Tailwind v4 엔트리

```css
@import "tailwindcss";

@theme {
  --font-sans: "Pretendard Variable", ...;
  --color-brand-500: oklch(0.62 0.18 255);
  /* ... */
}
```

Tailwind v4는 PostCSS 없이 Vite 플러그인(`@tailwindcss/vite`)으로 동작합니다. `@theme` 블록으로 디자인 토큰(브랜드 컬러, 폰트)을 선언하면 `bg-brand-500`, `font-sans` 같은 유틸리티가 자동 생성됩니다.

---

## 6. 자주 묻는 질문

### Q1. `app/api/` 가 Nuxt의 서버 API 라우트인가요?
아닙니다. **공식 컨벤션에 없는 우리 프로젝트만의 디렉토리**로, BE 응답 DTO와 매칭되는 TypeScript 타입만 모아둔 곳입니다. Nuxt의 서버 라우트는 `server/api/` 에 위치하지만 본 프로젝트는 `server/` 자체를 사용하지 않습니다.

### Q2. import 없이 `ref`, `useAsyncData`, `useNuxtApp` 이 동작하는 이유?
Nuxt가 Vue/Nuxt 내장 함수와 `app/composables/`, `app/utils/`, `app/components/` 의 export를 **자동 import** 해주기 때문입니다.

### Q3. `~/api/event` 의 `~` 는 어디인가요?
`app/app/` (Nuxt 4의 소스 루트) 입니다. `@` 도 같은 위치를 가리킵니다.

### Q4. 새 페이지를 추가하려면?
`app/pages/` 아래에 `.vue` 파일을 만들면 끝입니다. 폴더 구조가 곧 URL이고, `definePageMeta` 로 레이아웃을 지정합니다.

---

## 7. 개발 / 빌드

```bash
npm install        # 의존성 설치 (postinstall 로 nuxt prepare 자동 실행)
npm run dev        # 개발 서버: http://localhost:3000  (BE 는 :8080)
npm run build      # 프로덕션 빌드
npm run preview    # 빌드 결과 로컬 미리보기
npm run generate   # 정적 사이트 생성
```

BE(`http://localhost:8080`)가 같이 떠 있어야 정상 동작합니다.
