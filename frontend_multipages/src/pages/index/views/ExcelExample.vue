<template>
  <div class="about flex items-center">
    <h1>This is an example page about upload and download of Excel file</h1>
    <button @click="gotoHome"> 返回主页 </button>

  <!-- 导入文件按钮 后端端口路径：apiPath -->
  <el-card class="user-container">
      <template #header>
        <div class="header">
          <el-upload
              class="upload-demo"
              :action="state.importFileServer"
              :show-file-list="false"
              ref="upload"
              :limit="1"
              accept="xls,xlsx"
              :on-exceed="handleExceed"
              :before-upload="handleBeforeUpload"
              :on-success="handleUrlSuccess"
              style="display: inline-block"> <el-button type="primary">导入数据</el-button>
          </el-upload>&nbsp;&nbsp;
          <el-button type="success" icon="el-icon-upload"  @click="handleExcelExport">导出Excel</el-button>
        </div>
      </template>
      <Table action='/excel/list' ref="table">
        <template #column>
          <el-table-column align="center" type="selection"></el-table-column>
          <el-table-column prop="infantname" label="幼儿姓名" width="90"> </el-table-column>
          <el-table-column prop="nickname" label="昵称" width="80"> </el-table-column>
          <el-table-column prop="sex" label="性别" width="100"> </el-table-column>
          <el-table-column prop="dateof" label="出生日期" width="150"></el-table-column>
          <el-table-column prop="age" label="年龄" width="100"> </el-table-column>
          <el-table-column prop="nation" label="民族" width="100"> </el-table-column>
          <el-table-column prop="infantidnumber" label="身份证号" width="200"> </el-table-column>
          <el-table-column prop="birthplace" label="出生地"  width="200"> </el-table-column>
          <el-table-column prop="currentdate" label="保存时间"></el-table-column>
        </template>
      </Table>
  </el-card>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue'
import Table from '@/components/Table.vue'
import { ElMessage } from 'element-plus'
import axios from '@/utils/axios'
import { useRouter } from 'vue-router'
const router = useRouter()
let table = ref(null)
const state = reactive({
  importFileServer: 'api/excel/import',
})
const reload_list = () => {
  table.value.getList()
}


//导出
const handleExcelExport = () => {
  //后端路径
  window.open("api/excel/export");
  //this.$message.success("导出成功！");
}


const handleExceed = () => {
  this.$message({
    type:'warning',
    message:'超出最大上传文件数量的限制！'
  });
  return
}
const handleBeforeUpload = (file) => {
  const sufix = file.name.split('.')[1] || ''
  if (!['xls', 'xlsx'].includes(sufix)) {
    ElMessage.error('请上传xls,xlsx格式的文件')
    return false
  }
}
const handleUrlSuccess = (val) => {
  if(val.data > 0) {
    alert("共导入" + val.data.toString() + "条记录！");
    table.value.getList()
  }
  else alert("导入失败！");
}
const gotoHome = () => {
  router.push('/home')
}
</script>

<style>

</style>