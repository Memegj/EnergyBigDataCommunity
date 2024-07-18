<template>
  <div class="layout">
    <el-container v-if="state.showMenu" class="container">
        <Header />
        <div class="main">
          <router-view />
        </div>
        <Footer />
      </el-container>
    <el-container v-else class="container">
      <router-view />
    </el-container>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { localGet, pathMap } from '@/utils'

const noMenu = ['/login']
const router = useRouter()
const state = reactive({
  showMenu: true,
})

router.afterEach((to, from) => {
  state.showMenu = !noMenu.includes(to.path)
})

router.beforeEach((to, from, next) => {
  if (to.path == '/login') {
    // 如果路径是 /login 则正常执行
    next()
  } else {
    // 如果不是 /login，判断是否有 token
    if (!localGet('token')) {
      // 如果没有，则跳至登录页面
      next({ path: '/login' })
    } else {
      // 否则继续执行
      next({ path: '/login' })
    }
  }
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #ffffff;
}
.container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  max-height: 100vh;
  overflow: hidden;
  justify-content: right;
}
.main {
  height: calc(100vh - 100px);
  overflow: auto;
  padding: 10px;
}
body {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
a {
  color: #409eff;
  text-decoration: none;
}
</style>
