<template>
  <div class="home-container">
    <!-- Welcome section -->
    <div class="welcome-section">
      <h1 class="title">
        Welcome to the Tuition Management System
      </h1>
      <p class="subtitle">
        Efficiently manage your educational finances
      </p>

      <!-- Features grid -->
      <div class="features-grid">
        <div
            v-for="(feature, index) in features"
            :key="index"
            class="feature-card"
            :style="{ animationDelay: `${index * 0.2}s` }"
        >
          <!-- Each feature is wrapped in a router-link for navigation -->
          <router-link :to="feature.url" class="feature-link">
            <i :class="feature.icon"></i>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </router-link>
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
      // Features displayed on the home page
      features: [
        {
          icon: 'fas fa-users',
          title: 'User Management',
          description: 'Manage staff and admin accounts',
          url: '/user' // URL for navigation
        },
        {
          icon: 'fas fa-file-invoice-dollar',
          title: 'Tuition Billing',
          description: 'Handle student fees and payments',
          url: '/tuitionInvoice'
        },
        {
          icon: 'fas fa-user-graduate',
          title: 'Student Records',
          description: 'Track student information',
          url: '/student'
        },
        {
          icon: 'fas fa-chart-bar',
          title: 'Analytics',
          description: 'View financial statistics',
          url: '/analysis'
        }
      ]
    };
  }
};
</script>

<style scoped>
/* Container styles */
.home-container {
  min-height: 80vh;
  padding: clamp(1rem, 3vw, 2rem);
  margin: 0 auto;
}

/* Welcome section styles */
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
  font-size: clamp(1.1rem, 1.5vw, 1.5rem);
  color: #666;
  font-weight: 700;
  margin-bottom: clamp(2rem, 4vh, 3rem);
  animation: fadeInUp 0.8s ease-out;
}

/* Features grid styles */
.features-grid {
  display: grid;
  gap: clamp(2rem, 4vw, 4rem);
  padding: clamp(1rem, 2vw, 2rem);
  grid-template-columns: repeat(2, 1fr); /* Two cards per row */
  max-width: 1400px;
  height: 700px;
  margin: 0 auto;
}

/* Feature card styles */
.feature-card {
  height: 100%;
  background: #f8f4f4;
  padding: clamp(1.5rem, 2vw, 2rem);
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(21, 20, 20, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  animation: fadeInUp 0.5s ease-out forwards;
  min-height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.feature-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  text-decoration: none;
  color: inherit;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.feature-card i {
  font-size: clamp(1.8rem, 3vw, 2.2rem);
  color: #42b983; /* Icon color */
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

/* Animation definitions */
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

/* Responsive layout for smaller screens */
@media (max-width: 768px) {
  .features-grid {
    grid-template-columns: 1fr; /* One card per row */
    gap: 1rem;
    padding: 0 1rem;
  }

  .feature-card {
    min-height: 180px;
    padding: 1.5rem;
  }
}
</style>
