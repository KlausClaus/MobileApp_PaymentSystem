<template>
  <div class="home-container">
    <div class="welcome-section">
      <h1 class="title">
        Welcome to the Tuition Management System
      </h1>
      <p class="subtitle">
        Efficiently manage your educational finances
      </p>

      <div class="features-grid">
        <div v-for="(feature, index) in features"
             :key="index"
             class="feature-card"
             :style="{ animationDelay: `${index * 0.2}s` }">
          <i :class="feature.icon"></i>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      features: [
        {
          icon: 'fas fa-users',
          title: 'User Management',
          description: 'Manage staff and admin accounts'
        },
        {
          icon: 'fas fa-file-invoice-dollar',
          title: 'Tuition Billing',
          description: 'Handle student fees and payments'
        },
        {
          icon: 'fas fa-user-graduate',
          title: 'Student Records',
          description: 'Track student information'
        },
        {
          icon: 'fas fa-chart-bar',
          title: 'Analytics',
          description: 'View financial statistics'
        }
      ]
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  padding: clamp(1rem, 3vw, 2rem);
  margin: 0 auto;
  background: linear-gradient(145deg, #f8f9fa 0%, #ffffff 100%);
}

.welcome-section {
  text-align: center;
  padding: clamp(2rem, 4vh, 3rem) 0;
  max-width: 1800px;
  margin: 0 auto;
}

.title {
  font-size: clamp(2rem, 3vw, 2.8rem);
  color: #2c3e50;
  margin-bottom: clamp(0.5rem, 2vh, 1rem);
  font-weight: 700;
  animation: fadeInDown 0.8s ease-out;
}

.subtitle {
  font-size: clamp(1.1rem, 1.5vw, 1.3rem);
  color: #666;
  margin-bottom: clamp(2rem, 4vh, 3rem);
  animation: fadeInUp 0.8s ease-out;
}

.features-grid {
  display: grid;
  gap: clamp(1rem, 2vw, 2rem);
  padding: clamp(1rem, 2vw, 2rem);
  grid-template-columns: repeat(4, 1fr);
  max-width: 1800px;
  margin: 0 auto;
}

.feature-card {
  height: 100%;
  background: white;
  padding: clamp(1.5rem, 2vw, 2rem);
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  animation: fadeInUp 0.5s ease-out forwards;
  min-height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.feature-card i {
  font-size: clamp(1.8rem, 3vw, 2.2rem);
  color: #42b983;
  margin-bottom: 1rem;
  transition: transform 0.3s ease;
}

.feature-card:hover i {
  transform: scale(1.1) rotate(5deg);
}

.feature-card h3 {
  color: #2c3e50;
  margin-bottom: 0.8rem;
  font-weight: 600;
  font-size: clamp(1.1rem, 1.5vw, 1.2rem);
  text-align: center;
}

.feature-card p {
  color: #666;
  font-size: clamp(0.9rem, 1vw, 1rem);
  line-height: 1.5;
  max-width: 250px;
  text-align: center;
}

/* 动画定义 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式布局优化 */
@media (min-width: 1921px) {
  .welcome-section {
    max-width: 2000px;
  }

  .features-grid {
    grid-template-columns: repeat(4, 1fr);
    max-width: 1800px;
    margin: 0 auto;
    gap: 2.5rem;
  }

  .feature-card {
    padding: 2.5rem;
  }
}

@media (max-width: 1600px) {
  .features-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 2rem;
    padding: 0 2rem;
  }
}

@media (max-width: 1200px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1.5rem;
    padding: 0 1.5rem;
  }

  .feature-card {
    min-height: 180px;
  }
}

@media (max-width: 768px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
    padding: 0 1rem;
  }

  .feature-card {
    min-height: 180px;
    padding: 1.5rem;
  }
}

@media (max-width: 576px) {
  .features-grid {
    grid-template-columns: 1fr;
    padding: 0 1rem;
  }

  .feature-card {
    margin: 0 0.5rem;
    min-height: 160px;
    padding: 1.2rem;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .home-container {
    background: linear-gradient(145deg, #1a1a1a 0%, #2d2d2d 100%);
  }

  .title {
    color: #fff;
  }

  .subtitle {
    color: #bbb;
  }

  .feature-card {
    background: #2d2d2d;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }

  .feature-card:hover {
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.3);
  }

  .feature-card h3 {
    color: #fff;
  }

  .feature-card p {
    color: #bbb;
  }
}

/* 添加滚动时的平滑效果 */
html {
  scroll-behavior: smooth;
}
</style>