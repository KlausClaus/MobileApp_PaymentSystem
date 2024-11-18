<template>
  <div class="s-canvas">
    <canvas
        id="s-canvas"
        :width="contentWidth"
        :height="contentHeight"
    ></canvas>
  </div>
</template>
<script>
export default {
  name: "SIdentify",
  props: {
    // Verification code string
    identifyCode: {
      type: String,
      default: "1234", // Default verification code
    },
    // Minimum font size
    fontSizeMin: {
      type: Number,
      default: 25,
    },
    // Maximum font size
    fontSizeMax: {
      type: Number,
      default: 35,
    },
    // Minimum background color value
    backgroundColorMin: {
      type: Number,
      default: 200,
    },
    // Maximum background color value
    backgroundColorMax: {
      type: Number,
      default: 220,
    },
    // Minimum color value for interference dots
    dotColorMin: {
      type: Number,
      default: 60,
    },
    // Maximum color value for interference dots
    dotColorMax: {
      type: Number,
      default: 120,
    },
    // Width of the canvas
    contentWidth: {
      type: Number,
      default: 90,
    },
    // Height of the canvas
    contentHeight: {
      type: Number,
      default: 38,
    },
  },
  methods: {
    // Generates a random integer within the specified range
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },

    // Generates a random RGB color within the specified range
    randomColor(min, max) {
      const r = this.randomNum(min, max);
      const g = this.randomNum(min, max);
      const b = this.randomNum(min, max);
      return `rgb(${r}, ${g}, ${b})`;
    },

    // Draw the captcha on the canvas
    drawPic() {
      const canvas = document.getElementById("s-canvas");
      const ctx = canvas.getContext("2d");
      ctx.textBaseline = "bottom";

      // Draw the background
      ctx.fillStyle = "#e6ecfd";
      ctx.fillRect(0, 0, this.contentWidth, this.contentHeight);

      // Draw the verification code characters
      for (let i = 0; i < this.identifyCode.length; i++) {
        this.drawText(ctx, this.identifyCode[i], i);
      }

      // Add interference lines and dots
      this.drawLine(ctx);
      this.drawDot(ctx);
    },

    // Draw individual text characters on the canvas
    drawText(ctx, txt, i) {
      ctx.fillStyle = this.randomColor(50, 160); // Random text color
      ctx.font = `${this.randomNum(this.fontSizeMin, this.fontSizeMax)}px SimHei`; // Random font size

      const x = (i + 1) * (this.contentWidth / (this.identifyCode.length + 1));
      const y = this.randomNum(this.fontSizeMax, this.contentHeight - 5);
      const deg = this.randomNum(-30, 30); // Random rotation angle

      // Translate and rotate the canvas for the text
      ctx.translate(x, y);
      ctx.rotate((deg * Math.PI) / 180);
      ctx.fillText(txt, 0, 0);

      // Reset the canvas transformations
      ctx.rotate((-deg * Math.PI) / 180);
      ctx.translate(-x, -y);
    },

    // Draw interference lines on the canvas
    drawLine(ctx) {
      for (let i = 0; i < 4; i++) {
        ctx.strokeStyle = this.randomColor(100, 200);
        ctx.beginPath();
        ctx.moveTo(
            this.randomNum(0, this.contentWidth),
            this.randomNum(0, this.contentHeight)
        );
        ctx.lineTo(
            this.randomNum(0, this.contentWidth),
            this.randomNum(0, this.contentHeight)
        );
        ctx.stroke();
      }
    },

    // Draw interference dots on the canvas
    drawDot(ctx) {
      for (let i = 0; i < 30; i++) {
        ctx.fillStyle = this.randomColor(0, 255);
        ctx.beginPath();
        ctx.arc(
            this.randomNum(0, this.contentWidth),
            this.randomNum(0, this.contentHeight),
            1,
            0,
            2 * Math.PI
        );
        ctx.fill();
      }
    },
  },
  watch: {
    // Watch for changes in the verification code and redraw the canvas
    identifyCode() {
      this.drawPic();
    },
  },
  mounted() {
    // Draw the captcha when the component is mounted
    this.drawPic();
  },
};
</script>

