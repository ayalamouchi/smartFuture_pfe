/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        // Couleurs SmartFuture basées sur le logo
        'sf-navy': '#2C3E50',      // Bleu marine foncé du logo
        'sf-red': '#E74C3C',       // Rouge du logo
        'sf-blue': '#3498DB',      // Bleu vif
        'sf-dark': '#1A252F',      // Très foncé pour contraste
        'sf-gray': '#95A5A6',      // Gris pour textes secondaires
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
