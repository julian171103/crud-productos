// Address of our Spring Boot backend (same server, so no full URL is needed)
const API_URL = '/api/products';

// Below this number the stock is shown as "low"
const LOW_STOCK = 10;

// Get the elements from the HTML
const searchInput = document.getElementById('search-input');
const newButton = document.getElementById('new-button');
const message = document.getElementById('message');
const cardsContainer = document.getElementById('cards');

const detailPopup = document.getElementById('detail-popup');
const detailName = document.getElementById('detail-name');
const detailDescription = document.getElementById('detail-description');
const detailPrice = document.getElementById('detail-price');
const detailStock = document.getElementById('detail-stock');

const formPopup = document.getElementById('form-popup');
const formTitle = document.getElementById('form-title');
const productForm = document.getElementById('product-form');
const nameInput = document.getElementById('name-input');
const descriptionInput = document.getElementById('description-input');
const priceInput = document.getElementById('price-input');
const stockInput = document.getElementById('stock-input');
const formError = document.getElementById('form-error');

// The list that comes from the backend, and the product that is open in the pop up
let products = [];
let selectedProduct = null;

// The product that is being edited in the form (null when we are creating a new one)
let editingProduct = null;

// The backend sends the fields in English (name, description, price, stock),
// so we must use those same names when we read or send a product.


// ---------- GET: load all the products ----------
async function loadProducts() {
  try {
    const response = await fetch(API_URL);

    if (!response.ok) {
      message.textContent = 'Error del servidor: ' + response.status;
      return;
    }

    products = await response.json();
    showProducts();
  } catch (error) {
    message.textContent = 'No se pudo contactar con el servidor. Revisa que la aplicación siga corriendo.';
  }
}

// ---------- Show the cards (applies the search text) ----------
function showProducts() {
  const text = searchInput.value.trim().toLowerCase();

  // Keep only the products whose name or description contains the text
  const filtered = products.filter(function (product) {
    const name = product.name.toLowerCase();
    const description = (product.description || '').toLowerCase();
    return name.includes(text) || description.includes(text);
  });

  cardsContainer.innerHTML = '';

  if (products.length === 0) {
    message.textContent = 'Todavía no hay productos. Crea el primero.';
  } else if (filtered.length === 0) {
    message.textContent = 'No se encontraron productos.';
  } else {
    message.textContent = '';
  }

  for (const product of filtered) {
    cardsContainer.appendChild(createCard(product));
  }
}

// Text that describes the stock of a product
function stockText(product) {
  if (product.stock === 0) {
    return 'Agotado';
  }
  if (product.stock < LOW_STOCK) {
    return 'Stock bajo: ' + product.stock;
  }
  return product.stock + ' en stock';
}

// Create the card of one product
function createCard(product) {
  const card = document.createElement('div');
  card.className = 'card';

  const title = document.createElement('h3');
  title.textContent = product.name;

  const description = document.createElement('p');
  description.textContent = product.description || 'Sin descripción';

  const price = document.createElement('p');
  price.className = 'price';
  price.textContent = '$' + Number(product.price).toFixed(2);

  const stock = document.createElement('p');
  stock.textContent = stockText(product);
  if (product.stock === 0) {
    stock.className = 'sold-out';
  }

  card.appendChild(title);
  card.appendChild(description);
  card.appendChild(price);
  card.appendChild(stock);

  // Open the detail pop up when the card is clicked
  card.addEventListener('click', function () {
    openDetail(product);
  });

  return card;
}

// ---------- Detail pop up ----------
function openDetail(product) {
  selectedProduct = product;

  detailName.textContent = product.name;
  detailDescription.textContent = product.description || 'Sin descripción';
  detailPrice.textContent = 'Precio: $' + Number(product.price).toFixed(2);
  detailStock.textContent = 'Stock: ' + stockText(product);

  detailPopup.showModal();
}

// ---------- Form pop up (create or edit) ----------
function openForm(product) {
  editingProduct = product;
  productForm.reset();
  formError.textContent = '';

  if (product) {
    // Edit: fill the form with the data of the product
    formTitle.textContent = 'Editar producto';
    nameInput.value = product.name;
    descriptionInput.value = product.description || '';
    priceInput.value = product.price;
    stockInput.value = product.stock;
  } else {
    // Create: the form stays empty
    formTitle.textContent = 'Nuevo producto';
  }

  formPopup.showModal();
}

// ---------- POST / PUT: save the product ----------
async function saveProduct(event) {
  event.preventDefault(); // do not reload the page

  // The product that we send to the backend
  const product = {
    name: nameInput.value.trim(),
    description: descriptionInput.value.trim(),
    price: priceInput.value === '' ? null : Number(priceInput.value),
    stock: stockInput.value === '' ? null : Number(stockInput.value)
  };

  // Editing uses PUT with the id in the URL, creating uses POST
  const method = editingProduct ? 'PUT' : 'POST';
  const url = editingProduct ? API_URL + '/' + editingProduct.id : API_URL;

  try {
    const response = await fetch(url, {
      method: method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product)
    });

    if (!response.ok) {
      // The backend explains the problem in the "mensaje" field
      const error = await response.json();
      formError.textContent = error.mensaje || 'Error ' + response.status;
      return;
    }

    formPopup.close();
    await loadProducts();
  } catch (error) {
    formError.textContent = 'No se pudo contactar con el servidor.';
  }
}

// ---------- DELETE: remove the selected product ----------
async function deleteProduct() {
  const sure = confirm('¿Eliminar "' + selectedProduct.name + '"? No se puede deshacer.');
  if (!sure) {
    return;
  }

  try {
    const response = await fetch(API_URL + '/' + selectedProduct.id, { method: 'DELETE' });

    if (!response.ok) {
      alert('No se pudo eliminar el producto (error ' + response.status + ').');
      return;
    }

    detailPopup.close();
    await loadProducts();
  } catch (error) {
    alert('No se pudo contactar con el servidor.');
  }
}

// ---------- Connect the buttons ----------
searchInput.addEventListener('input', showProducts);
newButton.addEventListener('click', function () {
  openForm(null);
});

productForm.addEventListener('submit', saveProduct);
document.getElementById('cancel-button').addEventListener('click', function () {
  formPopup.close();
});
document.getElementById('form-close').addEventListener('click', function () {
  formPopup.close();
});

document.getElementById('detail-close').addEventListener('click', function () {
  detailPopup.close();
});
document.getElementById('edit-button').addEventListener('click', function () {
  detailPopup.close();
  openForm(selectedProduct);
});
document.getElementById('delete-button').addEventListener('click', deleteProduct);

// Close a pop up when clicking outside of it
for (const popup of [detailPopup, formPopup]) {
  popup.addEventListener('click', function (event) {
    if (event.target === popup) {
      popup.close();
    }
  });
}

// When the page opens, load the products
loadProducts();
