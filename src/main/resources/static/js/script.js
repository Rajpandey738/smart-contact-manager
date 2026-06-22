//--------------- Mobile Menu Toggle
const menuBtn = document.getElementById("menu-btn");
const mobileMenu = document.getElementById("mobile-menu");

if (menuBtn && mobileMenu) {
  menuBtn.addEventListener("click", () => {
    mobileMenu.classList.toggle("hidden");
  });
}

//--------------- Statical counter

const counters = document.querySelectorAll(".counter");
const statsSection = document.getElementById("stats-section");

let started = false;

function startCounter() {
  if (started) return;

  started = true;

  counters.forEach((counter) => {
    const target = +counter.getAttribute("data-target");

    const updateCounter = () => {
      const current = +counter.innerText.replace(/\D/g, "");

      const increment = target / 100;

      if (current < target) {
        counter.innerText = `${Math.ceil(current + increment)}`;

        setTimeout(updateCounter, 10);
      } else {
        if (target >= 1000) {
          counter.innerText = target / 1000 + "K+";
        } else if (target === 99) {
          counter.innerText = "99%";
        } else if (target === 24) {
          counter.innerText = "24/7";
        }
      }
    };

    updateCounter();
  });
}

window.addEventListener("scroll", () => {
  const sectionPosition = statsSection.getBoundingClientRect().top;
  const screenPosition = window.innerHeight - 100;

  if (sectionPosition < screenPosition) {
    startCounter();
  }
});

//--------------- Close popup on button click
const popup = document.getElementById("popup");
const closePopup = document.getElementById("closePopup");

// Check if popup exists
if (popup) {
  // Show popup
  popup.classList.remove("hidden");

  // Auto hide after 4 sec
  setTimeout(() => {
    popup.classList.add("hidden");
    fetch("/remove-message");
  }, 4000);

  // OK button close
  closePopup.addEventListener("click", () => {
    popup.classList.add("hidden");
    fetch("/remove-message");
  });
}

// --------------- Sidebar Toggle
const sidebar = document.getElementById("sidebar");
const sidebarMenuBtn = document.getElementById("sidebar-menu-btn");
const overlay = document.getElementById("overlay");

if (sidebarMenuBtn && sidebar && overlay) {
  sidebarMenuBtn.addEventListener("click", () => {
    sidebar.classList.toggle("-translate-x-full");
    overlay.classList.toggle("hidden");
  });

  overlay.addEventListener("click", () => {
    sidebar.classList.add("-translate-x-full");
    overlay.classList.add("hidden");
  });
}

//-------------------Delete Alert
document.querySelectorAll(".delete-contact").forEach((button) => {
  button.addEventListener("click", function (e) {
    e.preventDefault();

    const contactId = this.dataset.contactId;

    Swal.fire({
      title: "Delete Contact?",
      text: "This action cannot be undone!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#fb2c36",
      cancelButtonColor: "#364153",
      confirmButtonText: "Delete",
      cancelButtonText: "Cancel",
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = `/user/delete-contact/${contactId}`;
      }
    });
  });
});

// ---------------- Edit Profile Modal
const editProfileBtn = document.getElementById("editProfileBtn");
const editProfileModal = document.getElementById("editProfileModal");
const closeModalBtn = document.getElementById("closeModalBtn");
const cancelModalBtn = document.getElementById("cancelModalBtn");

if (editProfileBtn && editProfileModal && closeModalBtn && cancelModalBtn) {
  // Open Modal
  editProfileBtn.addEventListener("click", () => {
    editProfileModal.classList.remove("hidden");
  });

  // Close From X Button
  closeModalBtn.addEventListener("click", () => {
    editProfileModal.classList.add("hidden");
  });

  // Close From Cancel Button
  cancelModalBtn.addEventListener("click", () => {
    editProfileModal.classList.add("hidden");
  });

  // Close When Click Outside Modal
  editProfileModal.addEventListener("click", (e) => {
    if (e.target === editProfileModal) {
      editProfileModal.classList.add("hidden");
    }
  });
}

const changePhotoBtn = document.getElementById("changePhotoBtn");

const profilePicInput = document.getElementById("profilePicInput");

const profilePicForm = document.getElementById("profilePicForm");

if (changePhotoBtn && profilePicInput && profilePicForm) {
  changePhotoBtn.addEventListener("click", () => {
    profilePicInput.click();
  });

  profilePicInput.addEventListener("change", () => {
    if (profilePicInput.files.length > 0) {
      profilePicForm.submit();
    }
  });
}
