<template>
  <div class="hello">
    <!-- Display the message passed via props -->
    <h1>{{ msg }}</h1>

    <!-- Guide for project customization -->
    <p>
      For a guide and recipes on how to configure / customize this project,<br>
      check out the
      <a href="https://cli.vuejs.org" target="_blank" rel="noopener">vue-cli documentation</a>.
    </p>

    <!-- List of installed CLI plugins -->
    <h3>Installed CLI Plugins</h3>
    <ul>
      <li>
        <a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-babel" target="_blank" rel="noopener">
          babel
        </a>
      </li>
      <li>
        <a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-router" target="_blank" rel="noopener">
          router
        </a>
      </li>
    </ul>

    <!-- Useful links for Vue documentation and community -->
    <h3>Essential Links</h3>
    <ul>
      <li><a href="https://vuejs.org" target="_blank" rel="noopener">Core Docs</a></li>
      <li><a href="https://forum.vuejs.org" target="_blank" rel="noopener">Forum</a></li>
      <li><a href="https://chat.vuejs.org" target="_blank" rel="noopener">Community Chat</a></li>
      <li><a href="https://twitter.com/vuejs" target="_blank" rel="noopener">Twitter</a></li>
      <li><a href="https://classes.vuejs.org" target="_blank" rel="noopener">News</a></li>
    </ul>

    <!-- Links to Vue ecosystem resources -->
    <h3>Ecosystem</h3>
    <ul>
      <li><a href="https://router.vuejs.org" target="_blank" rel="noopener">vue-router</a></li>
      <li><a href="https://vuex.vuejs.org" target="_blank" rel="noopener">vuex</a></li>
      <li><a href="https://github.com/vuejs/vue-devtools#vue-devtools" target="_blank" rel="noopener">vue-devtools</a></li>
      <li><a href="https://vue-loader.vuejs.org" target="_blank" rel="noopener">vue-loader</a></li>
      <li><a href="https://github.com/vuejs/awesome-vue" target="_blank" rel="noopener">awesome-vue</a></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    // Message to be displayed as the main heading
    msg: String
  }
}
</script>

<style scoped>
/* Heading styles */
h3 {
  margin: 40px 0 0;
}

/* Unordered list styles */
ul {
  list-style-type: none;
  padding: 0;
}

/* List item styles */
li {
  display: inline-block;
  margin: 0 10px;
}

/* Anchor link styles */
a {
  color: #42b983; /* Vue brand color */
}
</style>
