import type { $Fetch } from 'ofetch'

// ───── BE 응답 계약 (plugin 내부에서만 사용) ─────

type SuccessResponse<T> = {
  code: number
  message: string
  data: T | null
}

type ErrorResponse = {
  code?: number
  message?: string
}

// useNuxtApp().$api 타입 추론용.
declare module '#app' {
  interface NuxtApp {
    $api: $Fetch
  }
}

export default defineNuxtPlugin(() => {
  const api = $fetch.create({
    baseURL: 'http://localhost:8080',

    // 네트워크 단절(서버 다운/DNS 실패/CORS preflight 실패 등) — 응답 자체가 없을 때
    onRequestError() {
      throw new Error('일시적인 오류가 발생했습니다.')
    },

    // 2xx: SuccessResponse 봉투 자동 언래핑
    onResponse({ response }) {
      const data = response._data
      if (data && typeof data === 'object' && 'data' in data) {
        response._data = (data as SuccessResponse<unknown>).data
      }
    },

    // 4xx/5xx: X-Error-Origin 헤더로 BE 응답 여부 판단 후 사용자 메시지로 throw
    onResponseError({ request, response }) {
      const fromBackend = response.headers.get('X-Response-Origin') === 'self'

      // 백엔드까지 도달하지 못한 경우
      if (!fromBackend) {
        console.error('[Network Error]', {
          request,
          status: response.status,
          response: response._data,
        })

        throw new Error('[Network] 일시적인 오류가 발생했습니다.')
      }

      const body = response._data as ErrorResponse | undefined
      const code = body?.code ?? String(response.status)

      // 서버 오류 (5xx)
      if (response.status >= 500) {
        console.error('[Server Error]', {
          request,
          status: response.status,
          code,
          response: body,
        })

        throw new Error(`[${code}] 서비스 처리 중 오류가 발생했습니다.`)
      }

      // 비즈니스 오류 (4xx)
      const message = body?.message

      if (!message) {
        console.warn('[Invalid Error Response]', {
          request,
          status: response.status,
          code,
          response: body,
        })

        throw new Error(`[${code}] 요청을 처리할 수 없습니다.`)
      }

      throw new Error(`[${code}] ${message}`)
    },
  })

  return { provide: { api } }
})
