import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'

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
      path: '/student/user_manage',
      name: 'user_manage',
      component: () => import('@/pages/student/views/UserManage.vue')
    },
    {
      path: '/student/collect',
      name: 'collect',
      component: () => import( '@/pages/student/views/Collectmanagement.vue')
    },
    {
      path: '/student/account',
      name: 'account',
      component: () => import( '@/pages/student/views/Account.vue')
    },
    {
      path: '/student/code_manage',
      name: 'code_manage',
      component: () => import( '@/pages/student/views/Code_management.vue')
    },
    {
      path: '/student/dataset_manage',
      name: 'dataset_manage',
      component: () => import( '@/pages/student/views/Datasets_management.vue')
    },
    {
      path: '/student/literature_manage',
      name: 'literature_manage',
      component: () => import( '@/pages/student/views/Literature_management.vue')
    },
    {
      path: '/student/video_manage',
      name: 'video_manage',
      component: () => import( '@/pages/student/views/Video_management.vue')
    },
    {
      path: '/student/code',
      name: 'code',
      component: () => import( '@/pages/student/views/Code.vue')
    },
    {
      path: '/student/dataset',
      name: 'dataset',
      component: () => import( '@/pages/student/views/Dataset.vue')
    },
    {
      path: '/student/literature',
      name: 'literature',
      component: () => import( '@/pages/student/views/Literature.vue')
    },
    {
      path: '/student/video',
      name: 'video',
      component: () => import( '@/pages/student/views/Video.vue')
    },
    {
      path: '/student/datasetupload',
      name: 'datasetupload',
      component: () => import( '@/pages/student/views/Datasetupload.vue')
    },
    {
      path: '/student/codeupload',
      name: 'codeupload',
      component: () => import( '@/pages/student/views/Codeupload.vue')
    },
    {
      path: '/student/literatureupload',
      name: 'literatureupload',
      component: () => import( '@/pages/student/views/Literatureupload.vue')
    },
    {
      path: '/student/videoupload',
      name: 'videoupload',
      component: () => import( '@/pages/student/views/Videoupload.vue')

    },

    {
      path: '/student/videocontentupload',
      name: 'videocontentupload',
      component: () => import( '@/pages/student/views/Videocontentupload.vue')

    },
    {
      path: '/student/datasetedit/:dataId', // 添加 dataId 参数
      name: 'datasetedit',
      component: () => import('@/pages/student/views/Datasetedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/student/codeedit/:codeId', // 添加 codeId 参数
      name: 'codeedit',
      component: () => import('@/pages/student/views/Codeedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/student/literatureedit/:literId', // 添加 codeId 参数
      name: 'literatureedit',
      component: () => import('@/pages/student/views/Literatureedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/student/videoedit/:videoId', // 添加 codeId 参数
      name: 'videoedit',
      component: () => import('@/pages/student/views/Videoedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/student/data_detail/:dataId',
      name: 'datasetDetail',
      component: () => import( '@/pages/student/views/DatasetDetail.vue'),
    },
    {
      path: '/student/code_detail/:codeId',
      name: 'codeDetail',
      component: () => import( '@/pages/student/views/CodeDetail.vue'),
    },
    {
      path: '/student/literature_detail/:literId',
      name: 'literatureDetail',
      component: () => import( '@/pages/student/views/LiteratureDetail.vue'),
    },
    {
      path: '/student/video_detail/:videoId',
      name: 'videoDetaile',
      component: () => import( '@/pages/student/views/VideoDetail.vue')
    },
  ],
})

export default router
