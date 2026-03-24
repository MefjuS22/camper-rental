import type { ReactNode } from "react";
import {
  AppBar,
  Avatar,
  Box,
  Breadcrumbs,
  Divider,
  IconButton,
  Link as MuiLink,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Paper,
  Toolbar,
  Typography
} from "@mui/material";
import DashboardRoundedIcon from "@mui/icons-material/DashboardRounded";
import DirectionsCarFilledRoundedIcon from "@mui/icons-material/DirectionsCarFilledRounded";
import EventNoteRoundedIcon from "@mui/icons-material/EventNoteRounded";
import NotificationsRoundedIcon from "@mui/icons-material/NotificationsRounded";
import { Link, Outlet, useRouterState } from "@tanstack/react-router";

import { useAuthStore } from "../../store/auth-store";

const DRAWER_WIDTH = 260;
const SIDEBAR_INNER_BG = "#0D0F17";

const navItems = [
  { to: "/", label: "Dashboard", icon: <DashboardRoundedIcon /> },
  { to: "/fleet", label: "Flota", icon: <DirectionsCarFilledRoundedIcon /> },
  { to: "/reservations", label: "Rezerwacje", icon: <EventNoteRoundedIcon /> }
] as const;

const pathLabels: Record<string, string> = {
  "/": "Dashboard",
  "/auth": "Logowanie",
  "/fleet": "Flota",
  "/reservations": "Rezerwacje"
};

type MainLayoutProps = {
  children?: ReactNode;
};

export function MainLayout({ children }: MainLayoutProps) {
  const auth = useAuthStore((state) => state.auth);
  const pathname = useRouterState({ select: (s) => s.location.pathname });
  const currentLabel = pathLabels[pathname] ?? "Panel";

  const avatarLetter = auth?.email?.charAt(0).toUpperCase() ?? "?";

  return (
    <Box
      sx={{
        minHeight: "100vh",
        bgcolor: "background.default",
        p: 2,
        display: "flex",
        gap: 2,
        alignItems: "stretch"
      }}
    >
      {/* Floating sidebar panel */}
      <Paper
        elevation={0}
        sx={{
          width: DRAWER_WIDTH,
          flexShrink: 0,
          borderRadius: "16px",
          overflow: "hidden",
          display: "flex",
          flexDirection: "column",
          bgcolor: SIDEBAR_INNER_BG,
          border: "1px solid rgba(255, 255, 255, 0.08)",
          alignSelf: "stretch"
        }}
      >
        <Toolbar sx={{ px: 2, minHeight: 64 }}>
          <Typography variant="h6" fontWeight={700} noWrap sx={{ color: "text.primary" }}>
            Caravaning ZSI
          </Typography>
        </Toolbar>
        <Divider sx={{ borderColor: "rgba(255,255,255,0.08)" }} />
        <List sx={{ px: 1, py: 2, flex: 1 }}>
          {navItems.map((item) => (
            <ListItemButton
              key={item.to}
              component={Link}
              to={item.to}
              selected={pathname === item.to}
              sx={{
                borderRadius: "12px",
                mb: 0.5,
                color: "text.secondary",
                "&.Mui-selected": {
                  bgcolor: "rgba(0, 240, 255, 0.12)",
                  color: "primary.main",
                  "&:hover": { bgcolor: "rgba(0, 240, 255, 0.16)" }
                },
                "&:hover": { bgcolor: "rgba(255, 255, 255, 0.06)" }
              }}
            >
              <ListItemIcon sx={{ color: "inherit", minWidth: 40 }}>{item.icon}</ListItemIcon>
              <ListItemText primaryTypographyProps={{ fontWeight: 600 }} primary={item.label} />
            </ListItemButton>
          ))}
        </List>
      </Paper>

      {/* Main column: glass header + content */}
      <Box
        sx={{
          flex: 1,
          minWidth: 0,
          display: "flex",
          flexDirection: "column",
          borderRadius: "16px",
          overflow: "hidden"
        }}
      >
        <AppBar
          position="sticky"
          elevation={0}
          sx={{
            bgcolor: "rgba(9, 10, 15, 0.55)",
            backdropFilter: "blur(10px)",
            WebkitBackdropFilter: "blur(10px)",
            borderBottom: "1px solid rgba(255, 255, 255, 0.08)",
            color: "text.primary"
          }}
        >
          <Toolbar sx={{ justifyContent: "space-between", gap: 2, minHeight: 64 }}>
            <Breadcrumbs aria-label="breadcrumb" sx={{ flex: 1 }} separator="›">
              <MuiLink
                component={Link}
                to="/"
                underline="hover"
                sx={{ color: "text.secondary", fontWeight: 600, "&:hover": { color: "primary.main" } }}
              >
                Panel
              </MuiLink>
              <Typography color="text.primary" fontWeight={600}>
                {currentLabel}
              </Typography>
            </Breadcrumbs>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <IconButton size="large" color="inherit" aria-label="Powiadomienia" sx={{ color: "text.secondary" }}>
                <NotificationsRoundedIcon />
              </IconButton>
              <Avatar
                sx={{
                  width: 40,
                  height: 40,
                  bgcolor: "transparent",
                  color: "primary.main",
                  border: "1px solid rgba(0, 240, 255, 0.35)",
                  fontWeight: 700
                }}
              >
                {avatarLetter}
              </Avatar>
            </Box>
          </Toolbar>
        </AppBar>

        <Box
          component="main"
          sx={{
            flex: 1,
            p: { xs: 2, sm: 3 },
            bgcolor: "background.default",
            overflow: "auto"
          }}
        >
          {children ?? <Outlet />}
        </Box>
      </Box>
    </Box>
  );
}
