import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/user': 'http://localhost:8080',
      '/article': 'http://localhost:8080',
      '/api': 'http://localhost:8080',
      '/email': 'http://localhost:8080',
      '/feedback': 'http://localhost:8080',
    },
  },
})
