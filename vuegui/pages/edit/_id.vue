<template lang="jade">
<div>
  <h1>Edit Bicycles</h1>
  <p>Name</p>

<div class="textfields">
  <label for="brand">Brand</label>
  <input v-model="brand" placeholder="Enter Brand">
  <br>
  <br>
  <label for="price">Price</label>
  <input v-model.number="price" placeholder="Enter Price">
  <br>
  <br>
  <label for="stock">Stock</label>
  <input v-model.number="stock" placeholder="Enter Stock">
  </div>

  <div class="button"><button :disabled="validInput" @click="updateBicycle()">Spara</button></div>
</div>

</div>
  </template>

<script>
export default {
  data() {
    return {
      brand: "mon",
      price: undefined,
      stock: undefined,
    };
  },
  created: function () {
    console.log("hej");
    this.fetchBicycle();
  },
  methods: {
    updateBicycle: function () {
      this.$axios
        .post("/cycles/" + this.$route.params.id + "/update", {
          bid: this.$route.params.id,
          brand: this.brand,
          price: this.price,
          stock: this.stock,
        })
        .then((response) => {
          this.$router.back()
        });
    },
    fetchBicycle: function () {
      this.$axios
        .get("/cycles/" + this.$route.params.id)
        .then((response) => { 
          let b = response.data[0]
          this.brand = b.brand
          this.price = b.price
          this.stock = b.stock
        });
    },
  },
};
</script>
  
  