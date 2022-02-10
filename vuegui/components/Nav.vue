<template>
  <div id="nav">
    <NuxtLink to="/">Home page</NuxtLink> |
    <NuxtLink to="/chat">Chat</NuxtLink> |
    <NuxtLink to="/bicycle">Bicycle</NuxtLink> |
    <NuxtLink to="/about">About</NuxtLink>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: whitesmoke;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: whitesmoke;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
