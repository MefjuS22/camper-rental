import React from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { RouterProvider } from "@tanstack/react-router";
import { CssBaseline } from "@mui/material";
import { ThemeProvider } from "@mui/material/styles";

import "./i18n/config";
import { I18nHtmlLang } from "./i18n/I18nHtmlLang";
import { router } from "./router";
import { theme } from "./theme";
import { useAuthStore } from "./store/auth-store";

const queryClient = new QueryClient();

function AppRouterProvider() {
  const auth = useAuthStore((state) => state.auth);
  return <RouterProvider router={router} context={{ auth }} />;
}

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <I18nHtmlLang />
        <CssBaseline />
        <AppRouterProvider />
      </ThemeProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
