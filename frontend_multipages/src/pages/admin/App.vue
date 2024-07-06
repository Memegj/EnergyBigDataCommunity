<template>
  <div class="layout">
    <el-container v-if="state.showMenu" class="container">
      <el-aside class="aside">
        <div class="head">
          <div>
            <img src="@/assets/创造太阳.png">
            <span>能源大数据教学科研平台</span>
          </div>
        </div>
        <div class="line" />
        <el-menu
            background-color="#e4e6ea"
            text-color="#000"
            :router="true"
            :default-openeds="state.defaultOpen"
            :default-active='state.currentPath'
        >
          <el-menu-item index="/admin/index"><el-icon><Odometer /></el-icon>首页</el-menu-item>

          <el-sub-menu index="2">
            <template #title>
              <span>代码</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin/mycharts" ><el-icon><Picture /></el-icon>Echarts</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <span>数据集</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin/file_upload"><el-icon><Menu /></el-icon>文件上传下载</el-menu-item>
              <el-menu-item index="/admin/references"><el-icon><Document-copy /></el-icon>参考文献</el-menu-item>
              <el-menu-item index="/admin/user_manage"><el-icon><User /></el-icon>用户管理</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <span>文献资料</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin/account"><el-icon><Lock /></el-icon>修改密码</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="5">
            <template #title>
              <span>教学视频</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin/gettable"><el-icon><Lock /></el-icon>表格练习</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group>
              <el-menu-item index="/admin/getfile"><el-icon><Lock /></el-icon>文件练习</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group>
              <el-menu-item index="/admin/getchart"><el-icon><Lock /></el-icon>图表练习</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="6">
            <template #title>
              <span>我的</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin/mycharts" ><el-icon><Picture /></el-icon>Echarts</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container class="content">
        <Header />
        <div class="main">
          <router-view />
        </div>
        <Footer />
      </el-container>
    </el-container>
    <el-container v-else class="container">
      <router-view />
    </el-container>
  </div>
</template>

<script setup>

import {onMounted, reactive} from 'vue'
import { useRouter } from 'vue-router'

import Header from '@/pages/admin/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { localGet, pathMap } from '@/utils'

const noMenu = ['/login']
const router = useRouter()
const state = reactive({
  showMenu: true,
  defaultOpen: ['1', '2', '3', '4'],
  currentPath: '/',
})

router.afterEach((to, from) => {
  state.showMenu = !noMenu.includes(to.path)
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    // 如果路径是 /login 则正常执行
    next()
  }
  else {
    // 如果不是 /login，判断是否有 token
    if (!localGet('token')) {
      // 如果没有，则跳至登录页面
      next({ path: '/login' })
    } else {
      // 否则继续执行
      next()
    }
  }

  state.currentPath = to.path
  //document.title = pathMap[to.name]
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #ffffff;
}
.container {
  height: 100vh;
}
.aside {
  width: 220px!important;
  background-color: #e4e6ea;
}
.head {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
}
.head > div {
  display: flex;
  align-items: center;
}

.head img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}
.head span {
  font-size: 15px;
  color: #01374d;
}
.line {
  border-top: 1px solid hsla(0,0%,100%,.05);
  border-bottom: 1px solid rgba(0,0,0,.2);
}
.content {
  display: flex;
  flex-direction: column;
  max-height: 100vh;
  overflow: hidden;
}
.main {
  height: calc(100vh - 100px);
  overflow: auto;
  padding: 10px;
}
</style>

<style>
body {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
.el-menu {
  border-right: none!important;
}
.el-sub-menu {
  border-top: 1px solid hsla(0, 0%, 100%, .05);
  border-bottom: 1px solid rgba(0, 0, 0, .2);
  .el-menu-item{
    height: 40px;
  }
  .el-menu-item:first-child{
    margin-top: -10px;
  }
}
.el-sub-menu:first-child {
  border-top: none;
}
.el-sub-menu [class^="el-icon-"] {
  vertical-align: -1px!important;
}
.el-sub-menu__title{
  height: 40px;
}
a {
  color: #409eff;
  text-decoration: none;
}
.el-pagination {
  text-align: center;
  margin-top: 20px;
}
.el-popper__arrow {
  display: none;
}
</style>