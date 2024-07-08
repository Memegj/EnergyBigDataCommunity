import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'
import About from '@/pages/index/views/ExcelExample.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/teacher/index'
    },
    {
      path: '/teacher/index',
      name: 'index',
      component: () => import( '@/pages/teacher/views/Index.vue')
    },
    {
      path: '/teacher/mycharts',
      name: 'mycharts',
      component: () => import( '@/pages/teacher/views/Chart_Example.vue')
    },
    {
      path: '/teacher/file_upload',
      name: 'file_upload',
      component: () => import( '@/pages/teacher/views/FileUpload.vue'),

    },
    {
      path: '/teacher/references',
      name: 'references',
      component: () => import( '@/pages/teacher/views/References.vue')
    },
    {
      path: '/teacher/user_manage',
      name: 'user_manage',
      component: () => import('@/pages/teacher/views/UserManage.vue')
    },
    {
      path: '/teacher/gettable',
      name: 'table',
      component: () => import( '@/pages/teacher/views/GetTable.vue')
    },
    {
      path: '/teacher/getfile',
      name: 'file',
      component: () => import( '@/pages/teacher/views/GetFile.vue')
    },
    {
      path: '/teacher/getchart',
      name: 'chart',
      component: () => import( '@/pages/teacher/views/GetChart.vue')
    }
  ],
})

export default router
