import { alpha, createTheme } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    mode: "dark",
    primary: {
      main: "#00F0FF",
    },
    secondary: {
      main: "#FF453A",
    },
    background: {
      default: "#090A0F",
      paper: "#161821",
    },
    text: {
      primary: "#FFFFFF",
      secondary: "#8B949E",
    },
    divider: "rgba(255, 255, 255, 0.08)",
  },
  shape: {
    borderRadius: 12,
  },
  typography: {
    fontFamily: '"Inter", sans-serif',
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          backgroundColor: "#090A0F",
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          boxShadow: "none",
          borderRadius: 16,
          border: "1px solid rgba(255, 255, 255, 0.08)",
          backgroundImage: "none",
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          boxShadow: "none",
          borderRadius: 16,
          border: "1px solid rgba(255, 255, 255, 0.08)",
          backgroundImage: "none",
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: "none",
          fontWeight: 600,
          borderRadius: "50px",
        },
        containedPrimary: {
          "&:hover": {
            boxShadow: `0 0 28px ${alpha("#00F0FF", 0.45)}`,
          },
        },
        containedSecondary: {
          "&:hover": {
            boxShadow: `0 0 28px ${alpha("#FF453A", 0.45)}`,
          },
        },
      },
    },
    MuiDrawer: {
      styleOverrides: {
        paper: {
          backgroundColor: "#0D0F17",
          borderRight: "none",
          boxShadow: "none",
          backgroundImage: "none",
        },
      },
    },
  },
});
