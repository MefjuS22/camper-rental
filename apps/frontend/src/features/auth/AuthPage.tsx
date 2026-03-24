import { useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useMutation } from "@tanstack/react-query";
import { login, register } from "@camper-rent/api-client";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Container,
  Stack,
  Tab,
  Tabs,
  TextField,
  Typography
} from "@mui/material";

import { env } from "../../utils/env";
import { useAuthStore } from "../../store/auth-store";

const formSchema = z.object({
  email: z.email("Invalid email format"),
  password: z.string().min(8, "Password must contain at least 8 characters")
});

type FormValues = z.infer<typeof formSchema>;

export function AuthPage() {
  const [tab, setTab] = useState<"login" | "register">("login");
  const auth = useAuthStore((state) => state.auth);
  const setAuth = useAuthStore((state) => state.setAuth);
  const clearAuth = useAuthStore((state) => state.clearAuth);

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: undefined
    }
  });

  const loginMutation = useMutation({
    mutationFn: (values: FormValues) => login(env.apiBaseUrl, values),
    onSuccess: (response) => setAuth(response)
  });

  const registerMutation = useMutation({
    mutationFn: (values: FormValues) => register(env.apiBaseUrl, values),
    onSuccess: (response) => setAuth(response)
  });

  const isLoading = loginMutation.isPending || registerMutation.isPending;
  const errorMessage = loginMutation.error?.message ?? registerMutation.error?.message ?? null;

  function onSubmit(values: FormValues) {
    if (tab === "login") {
      loginMutation.mutate(values);
      return;
    }
    registerMutation.mutate(values);
  }

  return (
    <Container maxWidth="sm" sx={{ py: 6 }}>
      <Stack spacing={3}>
        <Typography variant="h4" fontWeight={700}>
          Camper Rent Admin
        </Typography>

        <Card>
          <CardContent>
            <Tabs value={tab} onChange={(_, value: "login" | "register") => setTab(value)} sx={{ mb: 2 }}>
              <Tab value="login" label="Login" />
              <Tab value="register" label="Register" />
            </Tabs>

            <Box component="form" onSubmit={form.handleSubmit(onSubmit)}>
              <Stack spacing={2}>
                <TextField
                  label="Email"
                  type="email"
                  fullWidth
                  {...form.register("email")}
                  error={!!form.formState.errors.email}
                  helperText={form.formState.errors.email?.message}
                />
                <TextField
                  label="Password"
                  type="password"
                  fullWidth
                  {...form.register("password")}
                  error={!!form.formState.errors.password}
                  helperText={form.formState.errors.password?.message}
                />

                {errorMessage && <Alert severity="error">{errorMessage}</Alert>}

                <Button type="submit" variant="contained" disabled={isLoading}>
                  {isLoading ? <CircularProgress size={20} color="inherit" /> : tab === "login" ? "Login" : "Create account"}
                </Button>
              </Stack>
            </Box>
          </CardContent>
        </Card>

        <Card>
          <CardContent>
            <Stack spacing={1.5}>
              <Typography variant="h6">Current user</Typography>
              {!auth && <Typography color="text.secondary">No active session</Typography>}
              {auth && (
                <>
                  <Typography>
                    <strong>Public ID:</strong> {auth.publicId}
                  </Typography>
                  <Typography>
                    <strong>Email:</strong> {auth.email}
                  </Typography>
                  <Stack direction="row" spacing={1} flexWrap="wrap">
                    {auth.roles.map((role) => (
                      <Chip key={role} label={role} size="small" color="primary" variant="outlined" />
                    ))}
                  </Stack>
                  <Button variant="outlined" color="secondary" onClick={clearAuth} sx={{ alignSelf: "flex-start" }}>
                    Logout
                  </Button>
                </>
              )}
            </Stack>
          </CardContent>
        </Card>
      </Stack>
    </Container>
  );
}
