<template lang="jade">
<div>
<div><Nav></div>
  <div>
  <ul id="array-rendering">
    <li v-for="c in cycles">
      {{ c.id }} - {{c.brand}} - {{c.price}} - {{c.stock}} - <span @click= "deleteBicycle(c.id)"> x </span> 
    </li>
  </ul>
  </div>
  <input v-model="brand" placeholder="enter brand">
  <div><button @click="createBicycle()">Skapa</button></div>
</div>
</template>

<script>
export default {
  data() {
    return {
      cycles: [],
      brand: "",
    };
  },
  methods: {
    deleteBicycle: function (bid) {
      this.$axios
      .delete("/cycles/" + bid)
    },
    fetchBicycle: function () {
      this.$axios
        .get("/cycles")
        .then((response) => (this.cycles = response.data.cycles));
    },
    createBicycle: function () {
      this.$axios
        .post("/cycles", {
          brand: this.brand,
          price: 150,
          stock: 20,
        })
        .then((response) => this.fetchBicycle());
    },
  },
  created: function () {
    this.fetchBicycle();
  },
};
</script>