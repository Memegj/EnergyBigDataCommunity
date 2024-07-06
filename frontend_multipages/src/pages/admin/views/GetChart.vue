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
          //console.log(item);
          let arr = {
            value: item,
            label: item
            //label和value都是同一个人名
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
    title: {
      text: 'Distribution of Electricity',
      subtext: 'Fake Data'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    toolbox: {
      show: true,
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      // prettier-ignore
      data: ['00:00', '01:15', '02:30', '03:45', '05:00', '06:15', '07:30', '08:45', '10:00', '11:15', '12:30', '13:45', '15:00', '16:15', '17:30', '18:45', '20:00', '21:15', '22:30', '23:45']
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} W'
      },
      axisPointer: {
        snap: true
      }
    },
    visualMap: {
      show: false,
      dimension: 0,
      pieces: [
        {
          lte: 6,
          color: 'green'
        },
        {
          gt: 6,
          lte: 8,
          color: 'red'
        },
        {
          gt: 8,
          lte: 14,
          color: 'green'
        },
        {
          gt: 14,
          lte: 17,
          color: 'red'
        },
        {
          gt: 17,
          color: 'green'
        }
      ]
    },
    series: [
      {
        name: 'Electricity',
        type: 'line',
        smooth: true,
        // prettier-ignore
        data: state.valueData,
        markArea: {
          itemStyle: {
            color: 'rgba(255, 173, 177, 0.4)'
          },
          data: [
            [
              {
                name: 'Morning Peak',
                xAxis: '07:30'
              },
              {
                xAxis: '10:00'
              }
            ],
            [
              {
                name: 'Evening Peak',
                xAxis: '17:30'
              },
              {
                xAxis: '21:15'
              }
            ]
          ]
        }
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