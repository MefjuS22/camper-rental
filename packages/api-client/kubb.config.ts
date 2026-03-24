import { defineConfig } from "@kubb/core";
import { pluginOas } from "@kubb/plugin-oas";
import { pluginTs } from "@kubb/plugin-ts";
import { pluginZod } from "@kubb/plugin-zod";
import { pluginReactQuery } from "@kubb/plugin-react-query";

export default defineConfig({
  input: {
    path: "../../apps/backend/src/main/resources/openapi/openapi.yaml"
  },
  output: {
    path: "./src/generated",
    clean: true
  },
  plugins: [
    pluginOas(),
    pluginTs({
      output: {
        path: "./models"
      }
    }),
    pluginZod({
      output: {
        path: "./zod"
      }
    }),
    pluginReactQuery({
      output: {
        path: "./hooks"
      }
    })
  ]
});
