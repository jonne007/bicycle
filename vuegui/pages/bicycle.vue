<template lang="jade">
<div>
  <div>
  <ul id="array-rendering">
    <li v-for="c in cycles">
      {{ c.id }} - {{c.brand}} - {{c.price}} - {{c.stock}} - x // <---Länk för en delete
    </li>
  </ul>
  </div>
  <div><button @click="fetchBicycle">HämtaLista</button></div>
  <div><button @click="createBicycle('cresent')">Skapa</button></div>
</div>
</template>

<script>
export default {
  data() {
    return {
      cycles: [],
    };
  },
  methods: {
    fetchBicycle: function () {
      this.$axios
        .get("/cycles")
        .then((response) => (this.cycles = response.data.cycles));
    },
    createBicycle: function (brand) {
      this.$axios
        .post("/cycles", {
          id: "bid2",
          brand: brand,
          price: 150,
          stock: 20
        });
    },
  },
};
</script>