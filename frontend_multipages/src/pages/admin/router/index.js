import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'

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
      path: '/admin/code_manage',
      name: 'code_manage',
      component: () => import( '@/pages/admin/views/Code_management.vue')
    },
    {
      path: '/admin/dataset_manage',
      name: 'dataset_manage',
      component: () => import( '@/pages/admin/views/Datasets_management.vue')
    },
    {
      path: '/admin/literature_manage',
      name: 'literature_manage',
      component: () => import( '@/pages/admin/views/Literature_management.vue')
    },
    {
      path: '/admin/video_manage',
      name: 'video_manage',
      component: () => import( '@/pages/admin/views/Video_management.vue')
    },
    {
      path: '/admin/user_manage',
      name: 'user_manage',
      component: () => import('@/pages/admin/views/UserManage.vue')
    },
    {
      path: '/admin/collect',
      name: 'collect',
      component: () => import( '@/pages/teacher/views/Collectmanagement.vue')
    },
    {
      path: '/admin/account',
      name: 'account',
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
      path: '/admin/literature',
      name: 'literature',
      component: () => import( '@/pages/admin/views/Literature.vue')
    },
    {
      path: '/admin/video',
      name: 'video',
      component: () => import( '@/pages/admin/views/Video.vue')
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
      path: '/admin/literatureupload',
      name: 'literatureupload',
      component: () => import( '@/pages/admin/views/Literatureupload.vue')
    },
    {
      path: '/admin/videoupload',
      name: 'videoupload',
      component: () => import( '@/pages/admin/views/Videoupload.vue')
    },
    {
      path: '/admin/datasetedit/:dataId', // 添加 dataId 参数
      name: 'datasetedit',
      component: () => import('@/pages/admin/views/Datasetedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/admin/codeedit/:codeId', // 添加 codeId 参数
      name: 'codeedit',
      component: () => import('@/pages/admin/views/Codeedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/admin/literatureedit/:literId', // 添加 codeId 参数
      name: 'literatureedit',
      component: () => import('@/pages/admin/views/Literatureedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/admin/videoedit/:videoId', // 添加 codeId 参数
      name: 'videoedit',
      component: () => import('@/pages/admin/views/Videoedit.vue'),
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
    {
      path: '/admin/literature_detail/:literId',
      name: 'literatureDetail',
      component: () => import( '@/pages/admin/views/LiteratureDetail.vue'),
    },
    {
      path: '/admin/video_detail/:videoId',
      name: 'videoDetail',
      component: () => import( '@/pages/admin/views/VideoDetail.vue'),
    },
    {
      path: '/admin/videocontentedit/:videocontentId',
      name: 'videocontentedit',
      component: () => import('@/pages/admin/views/Videocontentedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/admin/videocontentupload',
      name: 'videocontentupload',
      component: () => import( '@/pages/admin/views/Videocontentupload.vue')
    },
  ],
})

export default router
