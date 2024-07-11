import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'
import About from '@/pages/index/views/ExcelExample.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/admin/index'
    },
    {
      path: '/admin/index',
      name: 'index',
      component: () => import( '@/pages/admin/views/Index.vue')
    },
    {
      path: '/admin/code',
      name: 'code',
      component: () => import( '@/pages/admin/views/Code.vue')
    },
    {
      path: '/admin/codeview',
      name: 'codeview',
      component: () => import( '@/pages/admin/views/CodeView.vue')
    },
    {
      path: '/admin/file_upload',
      name: 'file_upload',
      component: () => import( '@/pages/admin/views/FileUpload.vue'),

    },
    {
      path: '/admin/references',
      name: 'references',
      component: () => import( '@/pages/admin/views/References.vue')
    },
    {
      path: '/admin/user_manage',
      name: 'user_manage',
      component: () => import('@/pages/admin/views/UserManage.vue')
    },
    {
      path: '/admin/gettable',
      name: 'table',
      component: () => import( '@/pages/admin/views/GetTable.vue')
    },
    {
      path: '/admin/getfile',
      name: 'file',
      component: () => import( '@/pages/admin/views/GetFile.vue')
    },
    {
      path: '/admin/getchart',
      name: 'chart',
      component: () => import( '@/pages/admin/views/GetChart.vue')
    }
  ],
})

export default router
