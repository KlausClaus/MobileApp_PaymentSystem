<template>
  <div class="container">
    <div class="quadrant top-left">
      <div class="echart" id="myChart"></div>
    </div>
    <div class="quadrant top-right"><div class="echart" id="myChart1"></div></div>
    <div class="quadrant bottom-left"><div class="echart" id="myChart2"></div></div>
    <div class="quadrant bottom-right"><div class="echart" id="myChart3"></div></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'QuadrantLayout',
  data() {
    return {
      paymentData: [],
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.request.get("/tuitionInvoice/list").then(res => {
        this.paymentData = res.data;
        console.log(this.paymentData)
        this.initChart();
        this.initLineChart();
        this.initScatterChart();
        this.initPieChart();
      });
    },
    initChart() {
      const chartDom = document.getElementById('myChart');
      const myChart = echarts.init(chartDom);

      const paidCount = this.paymentData.filter(item => item.status === 1).length;
      const unpaidCount = this.paymentData.filter(item => item.status === 0).length;
      console.log(paidCount)
      const option = {
        title: {
          text: 'Tuition payment status statistics',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['paid', 'non-payment']
        },
        yAxis: {
          type: 'value',
          minInterval: 1
        },
        series: [
          {
            name: 'Number of people',
            type: 'bar',
            data: [paidCount, unpaidCount],
            itemStyle: {
              color: function(params) {
                return params.dataIndex === 0 ? '#91cc75' : '#ee6666';
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);

      // 响应式调整
      window.addEventListener('resize', function() {
        myChart.resize();
      });
    },
    initLineChart() {
      const chartDom = document.getElementById('myChart1');
      const myChart = echarts.init(chartDom);

      const feeRanges = [
        { min: 0, max: 1000, count: 0 },
        { min: 1000, max: 2000, count: 0 },
        { min: 2000, max: 3000, count: 0 },
        { min: 3000, max: 4000, count: 0 },
        { min: 4000, max: Infinity, count: 0 }
      ];

      this.paymentData.forEach(item => {
        const fee = item.totalFee;
        const range = feeRanges.find(range => fee >= range.min && fee < range.max);
        if (range) range.count++;
      });

      const option = {
        title: {
          text: 'Single payment',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['<1000', '1000-2000', '2000-3000', '3000-4000', '>4000']
        },
        yAxis: {
          type: 'value',
          minInterval: 1
        },
        series: [
          {
            name: 'Number of students',
            type: 'line',
            data: feeRanges.map(range => range.count),
            smooth: true
          }
        ]
      };

      myChart.setOption(option);
      window.addEventListener('resize', () => myChart.resize());
    },
    initScatterChart() {
      const chartDom = document.getElementById('myChart2');
      const myChart = echarts.init(chartDom);

      const paymentDelays = this.paymentData
          .filter(item => item.status === 1 && item.createdTime && item.paymentTime)
          .map(item => {
            const createdDate = new Date(item.createdTime);
            const paymentDate = new Date(item.paymentTime);
            const daysToPayment = Math.round((paymentDate - createdDate) / (1000 * 60 * 60 * 24));
            return [daysToPayment, item.totalFee, item.studentName];
          });

      const option = {
        title: {
          text: 'Student payment time distribution',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            return `student: ${params.data[2]}<br/>Delay in payment: ${params.data[0]}day<br/>Tuition fee: ${params.data[1]}`;
          }
        },
        xAxis: {
          type: 'value',
          name: 'Payment delay (days)',
          nameLocation: 'middle',
          nameGap: 30
        },
        yAxis: {
          type: 'value',
          name: 'Tuition Fee',
          nameLocation: 'middle',
          nameGap: 30
        },
        series: [
          {
            name: 'Time of payment',
            type: 'scatter',
            data: paymentDelays,
            symbolSize: function (data) {
              return Math.sqrt(data[0]) * 2 + 5; // 根据付款延迟调整点的大小
            },
            itemStyle: {
              color: function (params) {
                const delay = params.data[0];
                if (delay <= 7) return '#91cc75'; // 一周内
                if (delay <= 30) return '#fac858'; // 一个月内
                return '#ee6666'; // 超过一个月
              }
            }
          }
        ]
      };

      myChart.setOption(option);
      window.addEventListener('resize', () => myChart.resize());
    },
    initPieChart() {
      const chartDom = document.getElementById('myChart3');
      const myChart = echarts.init(chartDom);

      // 统计不同支付方式的人数
      const paymentMethods = {};
      this.paymentData.forEach(item => {
        if (item.paymentMethod) {
          paymentMethods[item.paymentMethod] = (paymentMethods[item.paymentMethod] || 0) + 1;
        }
      });

      // 转换数据格式为ECharts所需的格式
      const pieData = Object.entries(paymentMethods).map(([name, value]) => ({ name, value }));

      const option = {
        title: {
          text: 'Payment method statistics',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'Payment method',
            type: 'pie',
            radius: '50%',
            data: pieData,
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

      myChart.setOption(option);
      window.addEventListener('resize', () => myChart.resize());
    }
  }
};
</script>

<style scoped>
.container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 10px;
  height: 100%;
  width: 100%;
  max-width: 100vw;
  max-height: 100vh;
  box-sizing: border-box;
  padding: 10px;
}

.quadrant {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  border: 1px solid #ccc;
  overflow: hidden;
}

.top-left {
}

.top-right {
}

.bottom-left {
}

.bottom-right {
}

.echart {
  width: 100%;
  height: 100%;
}
</style>
