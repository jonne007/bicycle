<template lang="jade">
<div>
<div><Nav></div>
  <div class="header">
  <h1>Bicycle Super Mega Store</h1>
  </div>
  <div class="list">
  <ul id="array-rendering">
    <li v-for="c in cycles">
      {{ c.id }} - {{c.brand}} - {{c.price}} - {{c.stock}} - <span @click= "deleteBicycle(c.id)"> xxx </span>
      <span @click= "edit(id)"> Edit </span>
    </li>
  </ul>
  </div>
 

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

  
  <div class="button"><button :disabled="validInput" @click="createBicycle()">Skapa Cykel</button></div>
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
      if (this.brand != null&& this.brand.length > 2 && this.price >= 1 && this.stock >= 1) { 
        console.log("valid false")
        return false
      }
      else  
        return true
    },
  },
  methods: {
    edit(id) {
      this.$router.push({
        path: "/edit/" ,
      });
    },  
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

  <style> 
  body {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  margin: 0;
  background-color: #2675b5;
}
.header {
  padding: 60px;
  text-align: center;
  font-weight: bold;
  background: #1abc9c;
  color: whitesmoke;
  font-size: 30px;
}
.textfields {
  padding: 30px;
  text-align: center;
  font-weight: bold;
  background: #1abc9c;
  color: whitesmoke;
  font-size: 20px; 
}
.button {
  padding: 40px;
  text-align: center;
  font-weight: bold;
  background: #1abc9c;
  color: whitesmoke;
  font-size: 30px;
}
.list {
  padding: 40px;
  text-align: center;
  font-weight: bold;
  background: #1abc9c;
  color: whitesmoke;
  font-size: 30px;
}

</style>
