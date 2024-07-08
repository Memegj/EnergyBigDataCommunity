import { createRouter, createWebHashHistory } from 'vue-router'


const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/student',
      redirect: '/student/index'
    },
    {
      path: '/student/index',
      name: 'index',
      component: () => import( '@/pages/student/views/Index.vue')
    },
    {
      path: '/student/mycharts',
      name: 'mycharts',
      component: () => import( '@/pages/student/views/Chart_Example.vue')
    },
    {
      path: '/student/file_upload',
      name: 'file_upload',
      component: () => import( '@/pages/student/views/FileUpload.vue'),

    },
    {
      path: '/student/references',
      name: 'references',
      component: () => import( '@/pages/student/views/References.vue')
    },
    {
      path: '/student/user_manage',
      name: 'user_manage',
      component: () => import('@/pages/student/views/UserManage.vue')
    },
    {
      path: '/student/gettable',
      name: 'gettable',
      component: () => import( '@/pages/student/views/GetTable.vue')
    },
    {
      path: '/student/getfile',
      name: 'getfile',
      component: () => import( '@/pages/student/views/GetFile.vue')
    },
    {
      path: '/student/getchart',
      name: 'getchart',
      component: () => import( '@/pages/student/views/GetChart.vue')
    }
  ],
})

export default router
