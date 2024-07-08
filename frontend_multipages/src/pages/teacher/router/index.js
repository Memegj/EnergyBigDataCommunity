import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/student/views/Login.vue'
import Home from '@/pages/student/views/HomeView.vue'
import About from '@/pages/student/views/ExcelExample.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/teacher',
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
      name: 'gettable',
      component: () => import( '@/pages/teacher/views/GetTable.vue')
    },
    {
      path: '/teacher/getfile',
      name: 'getfile',
      component: () => import( '@/pages/teacher/views/GetFile.vue')
    },
    {
      path: '/teacher/getchart',
      name: 'getchart',
      component: () => import( '@/pages/teacher/views/GetChart.vue')
    }
  ],
})

export default router
