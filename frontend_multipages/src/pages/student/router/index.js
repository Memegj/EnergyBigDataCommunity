import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'
import About from '@/pages/index/views/ExcelExample.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
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
      name: 'table',
      component: () => import( '@/pages/student/views/GetTable.vue')
    },
    {
      path: '/student/getfile',
      name: 'file',
      component: () => import( '@/pages/student/views/GetFile.vue')
    },
    {
      path: '/student/getchart',
      name: 'chart',
      component: () => import( '@/pages/student/views/GetChart.vue')
    },
    {
      path: '/student/studentvideo',
      name: 'studentvideo',
      component: () => import( '@/pages/student/views/studentvideo.vue')
    },
    {
      path: '/video/:videoId',
      name: 'videoDetaile',
      component: () => import( '@/pages/student/views/VideoDetail.vue')
    },
    {
      path: '/student/videoManagement',
      name: 'videoManagement',
      component: () => import( '@/pages/student/views/videoManagement.vue')
    }
  ],
})

export default router;
