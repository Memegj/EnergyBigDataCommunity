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
      path: '/admin/dataset_manage',
      name: 'dataset_manage',
      component: () => import( '@/pages/admin/views/Datasets_management.vue')
    },
    {
      path: '/admin/code_manage',
      name: 'code_manage',
      component: () => import( '@/pages/admin/views/Code_management.vue')
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
    },
    {
      path: '/admin/account',
      name: 'chart',
      component: () => import( '@/pages/admin/views/Account.vue')
    },
    {
      path: '/admin/code',
      name: 'code',
      component: () => import( '@/pages/admin/views/Code.vue')
    },
    {
      path: '/admin/dataset',
      name: 'dataset',
      component: () => import( '@/pages/admin/views/Dataset.vue')
    },
    {
      path: '/admin/datasetupload',
      name: 'datasetupload',
      component: () => import( '@/pages/admin/views/Datasetupload.vue')
    },
    {
      path: '/admin/codeupload',
      name: 'codeupload',
      component: () => import( '@/pages/admin/views/Codeupload.vue')
    },
    {
      path: '/admin/datasetedit/:dataId', // 添加 dataId 参数
      name: 'datasetedit',
      component: () => import('@/pages/admin/views/Datasetedit.vue'),
      props: true // 通过 props 传递路由参数
    },
      {
          path: '/admin/codeedit/:codeId', // 添加 dataId 参数
          name: 'codeedit',
          component: () => import('@/pages/admin/views/Codeedit.vue'),
          props: true // 通过 props 传递路由参数
      },
    {
      path: '/admin/data_detail/:dataId',
      name: 'datasetDetail',
      component: () => import( '@/pages/admin/views/DatasetDetail.vue'),

    },
    {
      path: '/admin/code_detail/:codeId',
      name: 'codeDetail',
      component: () => import( '@/pages/admin/views/CodeDetail.vue'),

    },
  ],
})

export default router
