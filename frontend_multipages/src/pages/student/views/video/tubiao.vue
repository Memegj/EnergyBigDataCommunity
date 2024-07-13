
<script setup lang="ts">
import { ref } from 'vue'

defineProps<{ msg: string }>()

const count = ref(0)

import {
  Check,
  Delete,
  Edit,
  Message,
  Search,
  Star,
} from '@element-plus/icons-vue'


</script>

<template>
  <h1>{{ msg }}</h1>

  <div class="card">
    <button type="button" @click="count++">count is {{ count }}</button>
    <p>
      Edit
      <code>components/HelloWorld.vue</code> to test HMR
    </p>
  </div>
  <p>
    Check out
    <a href="https://vuejs.org/guide/quick-start.html#local" target="_blank">create-vue</a>, the official Vue + Vite starter
  </p>
  <p>
    Install
    <a href="https://github.com/vuejs/language-tools" target="_blank">Volar</a>
    in your IDE for a better DX
  </p>
  <p class="read-the-docs">Click on the Vite and Vue logos to learn more</p>



  <el-row class="mb-4">
    <el-button>Default默认</el-button>
    <el-button type="primary">主要</el-button>
    <el-button type="success">成功</el-button>
    <el-button type="info">信息</el-button>
    <el-button type="warning">警告</el-button>
    <el-button type="danger">危险</el-button>
  </el-row>
  <br />
  <el-row class="mb-4">
    <el-button plain>Plain普通 </el-button>
    <el-button type="primary" plain>主要</el-button>
    <el-button type="success" plain>成功</el-button>
    <el-button type="info" plain>信息</el-button>
    <el-button type="warning" plain>警告</el-button>
    <el-button type="danger" plain>危险</el-button>
  </el-row>
  <br />
  <el-row class="mb-4">
    <el-button round>Round圆</el-button>
    <el-button type="primary" round>主要</el-button>
    <el-button type="success" round>成功</el-button>
    <el-button type="info" round>信息</el-button>
    <el-button type="warning" round>警告</el-button>
    <el-button type="danger" round>危险</el-button>
  </el-row>
  <br />
  <el-row>
    <el-button :icon="Search" circle />
    <el-button type="primary" :icon="Edit" circle />
    <el-button type="success" :icon="Check" circle />
    <el-button type="info" :icon="Message" circle />
    <el-button type="warning" :icon="Star" circle />
    <el-button type="danger" :icon="Delete" circle />
  </el-row>

</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
 