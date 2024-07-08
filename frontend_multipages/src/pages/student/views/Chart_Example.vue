<template>
  <div id="nameSearch">
    <el-col :span="20">
      <el-form :inline="true">
        <el-form-item label="姓名">
          <el-select v-model="state.currentSelect" placeholder="请选择姓名" style="width: 200px">
            <el-option
                v-for="item in state.nameData" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item >
          <div style="text-align:right">
            <el-button type="primary" @click="searchHandler">查询</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-col>
  </div>
  <div id="chartRef" style="width: 600px; height: 400px"></div>
  <div id="static_chart" style="width: 600px; height: 400px"></div>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue'
import axios from '@/utils/axios'
import * as echarts from 'echarts';
//const chartRef = ref(null)

const state = reactive({
  nameData: [], // 选项
  weekData: [],
  valueData: [],
  currentSelect: ''
})

onMounted(() => {
  //绑定下拉框
  getNameOptions()
  show_static_chart()
})
const getNameOptions = () => {
  //读取和设置姓名下拉框
  axios.get('/chart_data/get_name_options')
      .then(function (res) {
        state.nameData = [];
        res.forEach(item => {
          console.log(item);
          let arr = {
            value: item,
            label: item
          };
          state.nameData.push(arr);
        });
      })
      .catch(function (err) {
        console.log(err)
      })
}
const searchHandler = () => {
  get_and_setData();
}
const get_and_setData = () => {
  //读取和设置week和value
  axios.get('/chart_data/get_data_by_name', {
    params: {
      currentSelect: state.currentSelect,
    }
  }).then(res => {
    state.weekData = res.weekData
    state.valueData = res.valueData
    showChart();
  })
}
const showChart = () => {
  const myChart = echarts.init(document.getElementById('chartRef'))

  const option = {
    title:{
      show:true,//false
      text: state.currentSelect + "的数据",
      left:'center',
      textStyle:{
        fontsize: 20,
      }
    },
    xAxis: {
      type: 'category',
      data: state.weekData//['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: state.valueData,//[820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line',
        smooth: true
      }
    ]
  };
  option && myChart.setOption(option);
}

const show_static_chart = () => {
  const myChart = echarts.init(document.getElementById('static_chart'))

  const option = {
    title: {
      text: '某站点用户访问来源',
      subtext: '纯属虚构',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
    },
    series: [
      {
        name: '访问来源',
        type: 'pie',
        radius: '55%',
        center: ['50%', '60%'],
        data: [
          {value: 335, name: '直接访问'},
          {value: 310, name: '邮件营销'},
          {value: 234, name: '联盟广告'},
          {value: 135, name: '视频广告'},
          {value: 1548, name: '搜索引擎'}
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  option && myChart.setOption(option);
}
</script>
<style scoped>

</style>