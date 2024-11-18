<template>
  <div class="container">
    <!-- Quadrants for displaying ECharts components -->
    <div class="quadrant top-left">
      <div class="echart" id="myChart"></div>
    </div>
    <div class="quadrant top-right">
      <div class="echart" id="myChart1"></div>
    </div>
    <div class="quadrant bottom-left">
      <div class="echart" id="myChart2"></div>
    </div>
    <div class="quadrant bottom-right">
      <div class="echart" id="myChart3"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'QuadrantLayout',
  data() {
    return {
      paymentData: [], // Holds payment-related data fetched from the server
    };
  },
  mounted() {
    this.fetchData(); // Fetch data when the component is mounted
  },
  methods: {
    // Fetches payment data from the API
    fetchData() {
      this.request.get("/tuitionInvoice/list").then(res => {
        this.paymentData = res.data; // Store fetched data
        console.log(this.paymentData);
        // Initialize all charts after data is fetched
        this.initChart();
        this.initLineChart();
        this.initScatterChart();
        this.initPieChart();
      });
    },
    // Initializes the bar chart for payment status statistics
    initChart() {
      const chartDom = document.getElementById('myChart');
      const myChart = echarts.init(chartDom);

      const paidCount = this.paymentData.filter(item => item.status === 1).length; // Paid count
      const unpaidCount = this.paymentData.filter(item => item.status === 0).length; // Unpaid count

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
          data: ['Paid', 'Non-payment']
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
              color: params => (params.dataIndex === 0 ? '#91cc75' : '#ee6666')
            }
          }
        ]
      };

      myChart.setOption(option);

      // Ensure chart resizes responsively
      window.addEventListener('resize', () => {
        myChart.resize();
      });
    },
    // Initializes the line chart for single payment distribution
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
    // Initializes the scatter chart for payment time distribution
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
          formatter: params =>
              `Student: ${params.data[2]}<br/>Delay in payment: ${params.data[0]} days<br/>Tuition fee: ${params.data[1]}`
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
            symbolSize: data => Math.sqrt(data[0]) * 2 + 5, // Adjust symbol size based on payment delay
            itemStyle: {
              color: params => {
                const delay = params.data[0];
                if (delay <= 7) return '#91cc75'; // Within one week
                if (delay <= 30) return '#fac858'; // Within one month
                return '#ee6666'; // More than one month
              }
            }
          }
        ]
      };

      myChart.setOption(option);
      window.addEventListener('resize', () => myChart.resize());
    },
    // Initializes the pie chart for payment methods
    initPieChart() {
      const chartDom = document.getElementById('myChart3');
      const myChart = echarts.init(chartDom);

      const paymentMethods = {};
      this.paymentData.forEach(item => {
        if (item.paymentMethod) {
          paymentMethods[item.paymentMethod] = (paymentMethods[item.paymentMethod] || 0) + 1;
        }
      });

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
/* Main container styles */
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

/* Styles for each quadrant */
.quadrant {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  border: 1px solid #ccc;
  overflow: hidden;
}

/* ECharts container styles */
.echart {
  width: 100%;
  height: 100%;
}
</style>
