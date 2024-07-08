import { createApp } from 'vue'
import {
    ElButton,
    ElContainer,
    ElAside,
    ElMenu,
    ElSubMenu,
    ElMenuItemGroup,
    ElMenuItem,
    ElForm,
    ElFormItem,
    ElInput,
    ElCheckbox,
    ElPopover,
    ElTag,
    ElCard,
    ElTable,
    ElTableColumn,
    ElPopconfirm,
    ElUpload,
    ElDialog,
    ElPagination,
    ElCascader,
    ElRadioGroup,
    ElRadio,
    ElSelect,
    ElOption
} from 'element-plus'
import App from './App.vue'
import router from '@/pages/teacher/router'
//import router from '@/router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//import * as echarts from 'echarts'  //引入Echarts，

const app = createApp(App) // 生成 Vue 实例 app

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.config.globalProperties.goTop = function () {
    const main = document.querySelector('.main')
    main.scrollTop = 0
}

app.use(router) // 引用路由实例

app.use(ElButton)
    .use(ElContainer)
    .use(ElAside)
    .use(ElMenu)
    .use(ElSubMenu)
    .use(ElMenuItemGroup)
    .use(ElMenuItem)
    .use(ElForm)
    .use(ElFormItem)
    .use(ElCheckbox)
    .use(ElInput)
    .use(ElPopover)
    .use(ElTag)
    .use(ElCard)
    .use(ElTable)
    .use(ElTableColumn)
    .use(ElPopconfirm)
    .use(ElUpload)
    .use(ElDialog)
    .use(ElPagination)
    .use(ElCascader)
    .use(ElRadioGroup)
    .use(ElRadio)
    .use(ElSelect)
    .use(ElOption)
app.mount('#app') // 挂载到 #app
