import { useRouter } from 'next/router';
import { useEffect } from 'react';
import { authService } from '@/services/authService';

interface ProtectedRouteProps {
    children: React.ReactNode;
    requiredRole?: string;
}

export default function ProtectedRoute({ children, requiredRole }: ProtectedRouteProps) {
    const router = useRouter();

    useEffect(() => {
        const checkAuth = () => {
            const isAuthenticated = authService.isAuthenticated();
            const currentUser = authService.getCurrentUser();

            if (!isAuthenticated) {
                router.push('/login');
                return;
            }

            if (requiredRole && currentUser?.role !== requiredRole) {
                router.push('/unauthorized');
            }
        };

        checkAuth();
    }, [router, requiredRole]);

    return <>{children}</>;
}