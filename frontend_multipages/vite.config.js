import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import path from "node:path";
import Components from "unplugin-vue-components/dist/vite.js";
import {ElementPlusResolver} from "unplugin-vue-components/dist/resolvers.js";
import ElementPlus from "unplugin-element-plus/vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    Components({
      resolvers: [ElementPlusResolver({
        importStyle: 'sass'
      })],
    }),
    ElementPlus()
  ],
  resolve: {
    alias: {
      '~': path.resolve(__dirname, './'),
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  baseURL: '/',
  server: {
    //这个地址是前端页面地址
    host: 'localhost',
    //前端页面端口
    port: 8084,
    proxy: {
      '/api': {
        target: 'http://localhost:8083/api', // 凡是遇到 /api 路径的请求，都映射到 target 属性
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '') // 重写 api 为 空，就是去掉它
      }
    }
  },
  build: {
    rollupOptions: {
      input:{
        student: resolve(__dirname, 'student.html'),
        teacher: resolve(__dirname, 'teacher.html'),
        index: resolve(__dirname, 'index.html'),
        admin: resolve(__dirname, 'admin.html'),
      }

    }},
  css: {
    preprocessorOptions: {
      // 覆盖掉element-plus包中的主题变量文件
      scss: {
        additionalData: `@use "@/styles/element/index.scss" as *;`,
      },
    },
  },
})
