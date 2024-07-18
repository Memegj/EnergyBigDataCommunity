import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/index/views/Login.vue'
import Home from '@/pages/index/views/HomeView.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/teacher/index'
    },
    {
      path: '/teacher/collect',
      name: 'collect',
      component: () => import( '@/pages/teacher/views/Collectmanagement.vue')
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
      path: '/teacher/stu_manage',
      name: 'stu_manage',
      component: () => import('@/pages/teacher/views/StuManage.vue')
    },
    {
      path: '/teacher/account',
      name: 'account',
      component: () => import( '@/pages/teacher/views/Account.vue')
    },
    {
      path: '/teacher/code_manage',
      name: 'code_manage',
      component: () => import( '@/pages/teacher/views/Code_management.vue')
    },
    {
      path: '/teacher/dataset_manage',
      name: 'dataset_manage',
      component: () => import( '@/pages/teacher/views/Datasets_management.vue')
    },
    {
      path: '/teacher/literature_manage',
      name: 'literature_manage',
      component: () => import( '@/pages/teacher/views/Literature_management.vue')
    },
    {
      path: '/teacher/video_manage',
      name: 'video_manage',
      component: () => import( '@/pages/teacher/views/Video_management.vue')
    },
    {
      path: '/teacher/code',
      name: 'code',
      component: () => import( '@/pages/teacher/views/Code.vue')
    },
    {
      path: '/teacher/dataset',
      name: 'dataset',
      component: () => import( '@/pages/teacher/views/Dataset.vue')
    },
    {
      path: '/teacher/literature',
      name: 'literature',
      component: () => import( '@/pages/teacher/views/Literature.vue')
    },
    {
      path: '/teacher/video',
      name: 'video',
      component: () => import( '@/pages/teacher/views/Video.vue')
    },
    {
      path: '/teacher/datasetupload',
      name: 'datasetupload',
      component: () => import( '@/pages/teacher/views/Datasetupload.vue')
    },
    {
      path: '/teacher/codeupload',
      name: 'codeupload',
      component: () => import( '@/pages/teacher/views/Codeupload.vue')
    },
    {
      path: '/teacher/literatureupload',
      name: 'literatureupload',
      component: () => import( '@/pages/teacher/views/Literatureupload.vue')
    },
    {
      path: '/teacher/videoupload',
      name: 'videoupload',
      component: () => import( '@/pages/teacher/views/Videoupload.vue')
    },
    {
      path: '/teacher/datasetedit/:dataId', // 添加 dataId 参数
      name: 'datasetedit',
      component: () => import('@/pages/teacher/views/Datasetedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/teacher/codeedit/:codeId', // 添加 codeId 参数
      name: 'codeedit',
      component: () => import('@/pages/teacher/views/Codeedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/teacher/literatureedit/:literId', // 添加 codeId 参数
      name: 'literatureedit',
      component: () => import('@/pages/teacher/views/Literatureedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/teacher/videoedit/:videoId', // 添加 codeId 参数
      name: 'videoedit',
      component: () => import('@/pages/teacher/views/Videoedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/teacher/data_detail/:dataId',
      name: 'datasetDetail',
      component: () => import( '@/pages/teacher/views/DatasetDetail.vue'),
    },
    {
      path: '/teacher/code_detail/:codeId',
      name: 'codeDetail',
      component: () => import( '@/pages/teacher/views/CodeDetail.vue'),
    },
    {
      path: '/teacher/literature_detail/:literId',
      name: 'literatureDetail',
      component: () => import( '@/pages/teacher/views/LiteratureDetail.vue'),
    },
    {
      path: '/teacher/video_detail/:videoId',
      name: 'videoDetail',
      component: () => import( '@/pages/teacher/views/VideoDetail.vue'),
    },
    {
      path: '/teacher/videocontentedit/:videocontentId',
      name: 'videocontentedit',
      component: () => import('@/pages/teacher/views/Videocontentedit.vue'),
      props: true // 通过 props 传递路由参数
    },
    {
      path: '/teacher/videocontentupload',
      name: 'videocontentupload',
      component: () => import( '@/pages/teacher/views/Videocontentupload.vue')
    },
    {
      path: '/teacher/newsManagement',
      name: 'newsManagement',
      component: () => import( '@/pages/teacher/views/NewsManagement.vue')
    },
    {
      path: '/teacher/AddNews',
      name: 'AddNews',
      component: () => import( '@/pages/teacher/views/AddNews.vue')
    }
  ],
})

export default router
