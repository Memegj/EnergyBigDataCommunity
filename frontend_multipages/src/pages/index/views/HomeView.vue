<template>
  <div class="about">
    <h1>This is an Home page</h1>
    <button @click="gotoExcel"> 进入Excel导入导出页面 </button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const gotoExcel = () => {
  router.push('/excelExample')
}
</script>


