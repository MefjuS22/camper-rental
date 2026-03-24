import React from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { CssBaseline, ThemeProvider, createTheme } from "@mui/material";
import { AuthPage } from "./features/auth/AuthPage";
import { AuthSessionSync } from "./features/auth/AuthSessionSync";

const queryClient = new QueryClient();
const theme = createTheme();

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthSessionSync />
        <AuthPage />
      </ThemeProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
