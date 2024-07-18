<template>
  <el-card class="account-container">
    <h1>系统简介</h1>
    <div style="line-height: 30px">
      能源大数据教学科研平台是面向科研师生的教学科研平台，实现文献资料、数据集、代码集、讲课视频等存储与分享，基于 Spring Boot 、Vue 3.0 相关技术栈开发。
    </div>
  </el-card>
  <div style="display: flex; height: 55vh; margin-top: 10px">
    <el-table :data="state.tableData" style="width: 45%;" >
      <el-table-column prop="title" label="新闻标题" width="300" >
        <template #default="scope">
          <a :href="scope.row.url" target="_blank" style="font-weight: bold; font-size: 16px">{{ scope.row.title }}</a>
        </template>
      </el-table-column>
      <el-table-column prop="releasetime" label="日期" width="200" class="bold-header"></el-table-column>
    </el-table>
    <DialogAddReference ref='addRef' :reload="table_data_reload" :type="state.type" />
    <el-divider direction="vertical" style="height: 80vh; border-left: 1px solid black;"></el-divider>
    <div>
      <div id="chartRef" style="width: 700px; height: 280px"></div>
      <div style="display: flex;">
        <div id="chartRef2" style="width: 350px; height: 280px"></div>
        <div id="static_chart" style="width: 350px; margin-left: 20px;margin-top: 0px; height: 280px"></div>
      </div>
    </div>
  </div>
  <div>
    <el-pagination
        small
        :total="state.total"
        :page-size="12"
        :current-page="state.currentPage"
        @current-change="changePage"
        style="float: left"
    />
  </div>
</template>

<script setup>
import { onMounted, reactive, watch, ref, onBeforeUnmount, shallowRef} from 'vue'
import axios from "axios";

import {handleCurrentChange} from "element-plus/es/components/tree/src/model/util";
import * as echarts from 'echarts';
import {ElMessage} from "element-plus";
const state2 = reactive({
  nameData: [], // 选项
  DownloadTimes: [],
  valueData: []
})

const show_dynamic_chart = () => {
  const myChart = echarts.init(document.getElementById('chartRef'));
  const option = {
    title: {
      show: true,
      text: '下载量排名前十的文献一览图',
      left: 'center',
      textStyle: {
        fontSize: 18,
      }
    },
    xAxis: {
      type: 'category',
      data: state2.nameData,
      axisLabel: {
        interval: 0,
        rotate: 20, // 标签旋转45度
        formatter: function (value) {
          return value.length > 5 ? value.slice(0, 5) + '...' : value;
        }
      },
      axisPointer: {
        show: true,
        type: 'shadow'
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: state2.DownloadTimes,
        type: 'bar',
        barWidth: '60%',
        itemStyle: {
          color: 'green'
        },
        label: {
          show: true, // Display values on top of bars
          position: 'top' // Position of the value label
        }
      }
    ]
  };
  option && myChart.setOption(option);
}
const getliterData=()=>{
  axios.get('/literature/get_chartdata').then(res => {
    state2.nameData = res.nameData
    state2.DownloadTimes = res.DownloadTimes
    show_dynamic_chart();
  })
}
const state3 = reactive({
  typeData: [], // 选项
  collectionsamount: [],
  valueData: []
})
const state3Ref = ref(state3); // 使用ref暴露state3给外部作用域


const show_static_chart = () => {
  try {
    const myChart = echarts.init(document.getElementById('static_chart'));

    const option = {
      title: {
        text: '各类资料收藏量占比',
        left: 'center',
        textStyle: {
          fontSize: 18,
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: state3.typeData
      },
      series: [
        {
          name: '访问来源',
          type: 'pie',
          radius: '55%',
          center: ['50%', '60%'],
          data: data,
          label: {
            normal: {
              formatter: '{b}\n{d}%',
              position: 'outside'
            }
          },
          labelLine: {
            normal: {
              length: 5, // 增加第一个部分的长度
              length2: 5, // 增加第二个部分的长度
              smooth: true
            }
          },
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
  } catch (error) {
    console.error('Error rendering pie chart:', error);
  }
}


let data = []; // 在更广泛的作用域内定义data

const getcollectdata = () => {
  axios.get('/collect/get_chartdata').then(res => {
    state3Ref.value.typeData = res.typeData;
    state3Ref.value.collectionsamount = res.collectionsamount;
    state3Ref.value.valueData = res;
    show_dynamic_chart2();

    data = state3Ref.value.typeData.map((type, index) => ({
      name: type,
      value: state3Ref.value.collectionsamount[index]
    }));
    console.log(data); // 在数据更新后打印data
    console.log(state3Ref.value.typeData); // 打印更新后的typeData
    console.log(state3Ref.value.collectionsamount); // 打印更新后的collectionsamount
    show_static_chart()
  })
}

const show_dynamic_chart2 = () => {
  const myChart2 = echarts.init(document.getElementById('chartRef2'));
  const option = {
    title: {
      show: true,
      text: '各类型资料收藏量展示',
      left: 'center',
      textStyle: {
        fontSize: 18,
      }
    },
    grid: {
      left: '20%', // 调整此值以增加左侧标签与图表之间的距离
    },
    xAxis: {
      type: 'category',
      data: state3.typeData,
      axisLabel: {
        interval: 0,
        formatter: function (value) {
          return value.length > 10 ? value.slice(0, 10) + '...' : value;
        }
      },
      axisPointer: {
        show: true,
        type: 'shadow'
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: state3.collectionsamount,
        type: 'bar',
        barWidth: '60%',
        itemStyle: {
          color: '#0C78B0'
        },
        label: {
          show: true, // Display values on top of bars
          position: 'top' // Position of the value label
        }
      }
    ]
  };
  option && myChart2.setOption(option);
}

let tableData2 = ref([]);
  const state = reactive({
    tableData: [],
    loading: false,
    multipleSelection: [], // 选中项
    total: 0, // 总条数
    currentPage: 1, // 当前页
    pageSize: 10, // 分页大小
    type: 'add' // 操作类型
  });
  const changePage = (val) => {
    state.currentPage = val
    fetchNewsData()
  }


  const fetchNewsData = async () => {
    state.loading = true
    axios.get("/news/getnew", {
      params: {
        pageNumber: state.currentPage,
        pageSize: state.pageSize,
      }
    }).then(res => {
      state.tableData = res.list;
      console.log(state.tableData);
      state.total = res.totalCount
      state.currentPage = res.currPage
      state.loading = false

    })
  }

  onMounted(() => {
    fetchNewsData();
    getliterData()
    getcollectdata()

  });

</script>

<style>

</style>