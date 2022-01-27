<template lang="jade">
<div>
<div><Nav></div>
  <div>
  <ul id="array-rendering">
    <li v-for="c in cycles">
      {{ c.id }} - {{c.brand}} - {{c.price}} - {{c.stock}} - <span @click= "deleteBicycle(c.id)"> xxx </span> 
    </li>
  </ul>
  </div>
  <div>
  <label for="brand">Brand</label>
  <input v-model="brand" placeholder="Enter Brand">
  <br>
  <input v-model.number="price" placeholder="Enter Price">
  <input v-model.number="stock" placeholder="Enter Stock">
  </div>
  <div><button :disabled="validInput" @click="createBicycle()">Skapa Cykel</button></div>
</div>
</template>

<script>
export default {
  data() {
    return {
      cycles: [],
      brand: "",
      price: undefined,
      stock: undefined,
    };
  },
  computed: {
    validInput: function () {
      if (this.brand != null&& this.brand.length > 2) { 
        console.log("valid false")
        return false
      }
      else  
        return true
    },
  },
  methods: {
    clearFields: function () { 
      this.brand = ""
      this.price = undefined
      this.stock = undefined
    },
    deleteBicycle: function (bid) {
      this.$axios
      .delete("/cycles/" + bid)
      .then((response) => { 
          this.fetchBicycle()
        });
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
          price: this.price,
          stock: this.stock,
        })
        .then((response) => { 
          this.fetchBicycle()
          this.clearFields()
        });
    },
  },
  created: function () {
    this.fetchBicycle();
  },
};
</script>