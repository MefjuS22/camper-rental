export type LoginRequestDto = {
  email: string;
  password: string;
};

export type RegisterRequestDto = {
  email: string;
  password: string;
};

export type JwtResponseDto = {
  token: string;
  publicId: string;
  email: string;
  roles: string[];
};

export type CurrentUserDto = {
  publicId: string;
  email: string;
  roles: string[];
};

export async function login(baseUrl: string, payload: LoginRequestDto): Promise<JwtResponseDto> {
  const response = await fetch(`${baseUrl}/api/v1/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    throw await toApiError(response);
  }

  return (await response.json()) as JwtResponseDto;
}

export async function register(baseUrl: string, payload: RegisterRequestDto): Promise<JwtResponseDto> {
  const response = await fetch(`${baseUrl}/api/v1/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    throw await toApiError(response);
  }

  return (await response.json()) as JwtResponseDto;
}

export async function me(baseUrl: string, token: string): Promise<CurrentUserDto> {
  const response = await fetch(`${baseUrl}/api/v1/auth/me`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!response.ok) {
    throw await toApiError(response);
  }

  return (await response.json()) as CurrentUserDto;
}

async function toApiError(response: Response): Promise<Error> {
  const fallbackMessage = `Request failed with status ${response.status}`;
  try {
    const payload = (await response.json()) as { detail?: string };
    return new Error(payload.detail ?? fallbackMessage);
  } catch {
    return new Error(fallbackMessage);
  }
}
