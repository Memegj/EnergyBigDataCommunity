<template>
  <div class="header">
    <div class="right">
      <el-popover
        placement="bottom"
        :width="320"
        trigger="click"
        popper-class="popper-user-box"
      >
        <template #reference>
          <div class="author">
            <el-link type="primary">
              <i class="icon el-icon-s-custom" />
              {{ state.userInfo && state.userInfo.nickname ||''}}
              <i class="el-icon-caret-bottom" />
            </el-link>
          </div>
        </template>
        <div class="nickname">
          <p>登录名：{{state.userInfo && state.userInfo.username ||'' }}</p>
          <p>昵称：{{ state.userInfo && state.userInfo.nickname ||'' }}</p>

        </div>
      </el-popover>
      <div class="lgout">
        <el-button type="danger" size="8" @click="logout">退出</el-button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'

import axios from '@/utils/axios'
import {localGet, localRemove, localSet, pathMap} from '@/utils'

const router = useRouter()
const state = reactive({
  userInfo: null, // 用户信息变量
})
// 初始化执行方法
onMounted(() => {
  const pathname = window.location.hash.split('/')[1] || ''
  if(!['login'].includes(pathname) && localGet('token')!=null){
    getUserInfo()

  }
  else  router.push({ path: '/login' })
})
//获取当前用户信息
const getUserInfo = ()=>{
  axios.get('/user/profile').then(res => {
    state.userInfo = res

  })

}
// 退出登录
const logout = () => {
  axios.delete('/user/logout').then(() => {
    // 退出之后，将本地保存的 token  清理掉
    localRemove('token')
    // 回到登录页
    router.push({ path: '/login' })
  })
}

</script>

<style scoped>
  .header {
    height: 50px;
    border-bottom: 1px solid #e9e9e9;
    display: flex;
    justify-content: right;
    align-items: center;
    padding: 0 20px;
  }
  .header .left .back {
    border: 1px solid #e9e9e9;
    padding: 5px;
    border-radius: 50%;
    margin-right: 5px;
    cursor: pointer;
  }
  .right{
    display: flex;
  }
  .right > div {
    font-size: 18px;
    margin-right: 6px;
  }
  .author {
    margin-left: 10px;
    cursor: pointer;
  }
  .lgout{
    cursor: pointer;
    margin-left: 10px;
  }
</style>

<style>
  .popper-user-box {
    background: url("@/assets/account-banner-bg.png") 50% 50% no-repeat!important;
    background-size: cover!important;
    border-radius: 0!important;
  }
   .popper-user-box .nickname {
    position: relative;
    color: #ffffff;
  }
  .popper-user-box .nickname .logout {
    position: absolute;
    right: 0;
    top: 0;
    cursor: pointer;
  }
</style>