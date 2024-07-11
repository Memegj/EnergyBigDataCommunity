import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'
import About from '@/pages/index/views/ExcelExample.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/home',
      name: 'home',
      component: Home,
    },
    {
      path: '/about',
      name: 'about',
      component: About,
    },
    {
      path: '/admin',
      redirect: '/admin/index'
    },
    {
      path: '/admin/index',
      name: 'index',
      component: () => import( '@/pages/admin/views/Index.vue')
    },
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
      path: '/teacher',
      redirect: '/teacher/index'
    },
    {
      path: '/teacher/index',
      name: 'index',
      component: () => import( '@/pages/teacher/views/Index.vue')
    },
    {
      path: '/teacher/team',
      name: 'team',
      component: () => import( '@/pages/teacher/views/Team.vue')
    },
    {
      path: '/admin/file_upload',
      name: 'file_upload',
      component: () => import( '@/pages/admin/views/FileUpload.vue'),

    },
    {
      path: '/admin/code',
      name: 'code',
      component: () => import( '@/pages/admin/views/Code.vue'),

    },
    {
      path: '/admin/codeview',
      name: 'codeview',
      component: () => import( '@/pages/admin/views/CodeView.vue'),

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
      path: '/admin/account',
      name: 'account',
      component: () => import( '@/pages/admin/views/Account.vue')
    },
    {
      path: '/admin/gettable',
      name: 'gettable',
      component: () => import( '@/pages/admin/views/GetTable.vue')
    },
    {
      path: '/admin/getfile',
      name: 'getfile',
      component: () => import( '@/pages/admin/views/GetFile.vue')
    },
    {
      path: '/admin/getchart',
      name: 'getchart',
      component: () => import( '@/pages/admin/views/GetChart.vue')
    },
  ],
})

export default router
