// https://nuxt.com/docs/api/configuration/nuxt-config
import tailwindcss from '@tailwindcss/vite'

export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: {enabled: true},

  modules: ['@pinia/nuxt'],

  // Tailwind v4 의 CSS 엔트리. 공식 가이드대로 @import "tailwindcss"; 한 줄만 들어있음.
  css: ['~/assets/css/main.css'],

  // Tailwind v4 공식 Vite 플러그인. PostCSS 방식보다 빠르고 zero-config.
  vite: {
    plugins: [tailwindcss()],
  },
})
